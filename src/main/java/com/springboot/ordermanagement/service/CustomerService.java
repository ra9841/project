package com.springboot.ordermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.ordermanagement.dao.CustomerDao;
import com.springboot.ordermanagement.model.Customer;
import com.springboot.ordermanagement.request.CustomerRequest;

@Service // this tells this class contains business logic
public class CustomerService {
	
	Logger logger = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private CustomerDao customerDao;
	
	public Customer saveCustomer(CustomerRequest customerRequest) {
		// take all the request fields from customerRequest and set it in customer(model) object
		// then save customer model object into database and return it
		logger.info("saveCustomer method started");
		Customer customer = new Customer();
		customer.setCustomerName(customerRequest.getCustomerName());
		customer.setEmail(customerRequest.getEmail());
		customer.setMobile(customerRequest.getMobile());
		customer.setPassword(customerRequest.getPassword());
		customer.setAddress(customerRequest.getAddress());
		
		customer = customerDao.save(customer);
		logger.info("saved succesfully");
		if(customer == null) {
			logger.info("customer not saved excecption occured");
			throw new RuntimeException("Customer not saved!");
		}
		return customer;
	}
	
	public Customer getCustomerById(Long customerId) {
		logger.info("getCustomerById method has started");
		// Optional - used to check value can be present or not present
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		if(!customerOptional.isPresent()) {
			logger.error("no customer found with given id");
			throw new RuntimeException("Customer not found in database!");
		}
		Customer customer = customerOptional.get();
		logger.info("getCustomerById method has ended");
		return customer;
		
	}
	
	public List<Customer> getAllCustomers() {
		List<Customer> customerList = customerDao.findAll();
		if(customerList.isEmpty()) {
			throw new RuntimeException("No customer present in database");
		}
		return customerList;
	}
	
	/*
	 * pagination - fetching the records based on pages
	 * two parameters - page number, page size
	 * pagenumber - number of page requested
	 * pagesize - how many records needs to be present in each page
	 * 
	 * pagesize -10, number of records -36
	 * 0th page - 1-10
	 * 1st page - 11-20
	 * 2nd page - 21-30
	 * 3rd page - 31-36
	 * 
	 * pagesize-3, number of records-25
	 * 0th page - 1-3
	 * 1st page - 4-6
	 * 2nd page - 7-9
	 * 3rd page - 10-12
	 * 4th page - 13-15
	 * 5th page - 16-18
	 * 6th page - 19-21
	 * 7th page - 22-24
	 * 8th page - 25
	 */
	public List<Customer> getAllCustomersWithPagination(Integer pageNumber, Integer pageSize) {
		
		Page<Customer> pageCustomer = customerDao.findAll(PageRequest.of(pageNumber, pageSize,Sort.by("customerName").descending()));
		// we need to convert page to list of customers and return list as response
		List<Customer> customerList = new ArrayList<>();
		for(Customer cust:pageCustomer) {
			customerList.add(cust);
		}
		if(customerList.isEmpty()) {
			throw new RuntimeException("No customer present in database");
		}
		return customerList;
	}
	
	public void deleteCustomerById(Long customerId) {
		customerDao.deleteById(customerId);
	}
	
	public Customer getCustomerWithEmail(String email) {
		Customer customer = customerDao.findCustomerByEmail(email);
		if(customer==null) {
			throw new RuntimeException();
		}
		return customer;
	}
	
	public List<Customer> getCustomerWithSameCountry(String countryname){
		List<Customer> customerList = customerDao.findCustomerWithSameCountry(countryname);
		if(customerList.isEmpty()) {
			throw new RuntimeException();
		}
		return customerList;
	}
	
	public Customer updateCustomer(Long customerId, CustomerRequest newCustomerRequest) {
		// 1. get old customer details present in database using findbyid
		// 2. remove oldcustomer details -> set old customer details with newcustomerrequest
		// 3. save the new customer updated details and return it
		
		Customer updatedCustomer = null;
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		if(customerOptional.isPresent()) {
			// oldcustomer will have already existing details present in database
			// newcustomerrequest will have new values which needs to be updated in database
			Customer oldCustomer = customerOptional.get();
			// oldcustomer values will be overridden by newcustomwerequest values
			oldCustomer.setCustomerName(newCustomerRequest.getCustomerName());
			oldCustomer.setEmail(newCustomerRequest.getEmail());
			oldCustomer.setMobile(newCustomerRequest.getMobile());
			oldCustomer.setAddress(newCustomerRequest.getAddress());
			oldCustomer.setPassword(newCustomerRequest.getPassword());
			// oldcustomer will now have newupdated values
			updatedCustomer = customerDao.save(oldCustomer);
			if(updatedCustomer==null) {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException("Customer not found!");
		}
		
		return updatedCustomer;
	}
	
	public long countCustomers() {
		long totalcustomer = customerDao.count();
		if(totalcustomer == 0) {
			throw new RuntimeException();
		}
		return totalcustomer;
	}
	
}
