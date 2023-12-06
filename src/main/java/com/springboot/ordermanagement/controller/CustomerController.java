package com.springboot.ordermanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ordermanagement.model.Customer;
import com.springboot.ordermanagement.request.CustomerRequest;
import com.springboot.ordermanagement.response.CustomerResponse;
import com.springboot.ordermanagement.service.CustomerService;

@RestController
@RequestMapping(value="/om/v1/customer")
public class CustomerController {

	/*
	 * API - Application programming interface - communication between request and response (entry point or where our request starts executing)
	 * REST API - representational state Transfer API - to handle HTTP web requests
	 * 
	 * create/save - post
	 * update - put/patch
	 * read/retrieve - get
	 * delete - delete
	 */
	/*
	 * Loggers  - used to log message for the application which helps in tracing the applicatio
	 * It maintains the hierarchical history of the application
	 * 4 levels of loggers
	 * info - to give some information about application
	 * error - to display errors
	 * warn - to give warning messages
	 * debug - to run in debug mode and display log
	 */
	
	/*
	 * Rest Template - calling one API inside another API
	 */
	
	Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	// @RequestBody - it takes the input from front end in json format and passes it to API
	@PostMapping(value="/save") // other way - @RequestMapping(value="/save", method=RequestMethod.POST)
	public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest) {
		logger.info("createCustomer API has started");
		CustomerResponse customerResponse = new CustomerResponse();
		try {
			Customer savedCustomer = customerService.saveCustomer(customerRequest);
			logger.info("customer saved successfully");
			customerResponse.setData(savedCustomer);
			customerResponse.setMessage("Customer Saved successsfully into database!");
			customerResponse.setCode(HttpStatus.OK.toString());
			logger.info("createCustomer API ended");
			return ResponseEntity.ok().body(customerResponse);
		} catch (Exception e) {
			logger.error("customer cannot be saved, an exception occured : "+e.getMessage());
			customerResponse.setData(null);
			customerResponse.setMessage("Customer could not be saved!");
			customerResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			return ResponseEntity.internalServerError().body(customerResponse);
		}	
	}
	
	//@PathVariable - value comes within url
	@GetMapping(value="/find/{customerid}")
	public ResponseEntity<?> getCustomerById(@PathVariable("customerid") Long customerId) {
		logger.info("getCustomerById API has started");
		try {
			Customer customer = customerService.getCustomerById(customerId);
			logger.info("customer fetched successfully and getCustomerById API has ended");
			return ResponseEntity.ok().body(customer);
		}catch (Exception e) {
			logger.error("customer did not found");
			return ResponseEntity.internalServerError().body("Customer trying to find is not found inside database!");
		}
	}
	
	@GetMapping(value="/findall")
	public ResponseEntity<?> getAllCustomer() {
		try {
			List<Customer> custList = customerService.getAllCustomers();
			return ResponseEntity.ok().body(custList);
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("No customers found!");
		}
	}
	
	@GetMapping(value="/findallwithpage")
	public ResponseEntity<?> getAllCustomerWithPagination(@RequestParam("pagesize") Integer pageSize, @RequestParam("pageno") Integer pageNumber) {
		try {
			List<Customer> customerList = customerService.getAllCustomersWithPagination(pageNumber, pageSize);
			return ResponseEntity.ok().body(customerList);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body("No customers found!");
		}
	}
	
	@DeleteMapping(value="/delete/{customerid}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable("customerid") Long customerId) {
		try {
			customerService.deleteCustomerById(customerId);
			return ResponseEntity.ok().body("The customer with customerId "+customerId+" is deleted successfully!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("The customer with customerId "+customerId+" is not found!");
		}
	}
	
	@GetMapping(value="/findbyemail")
	public ResponseEntity<?> getCustomerWithEmail(@RequestParam("email") String email) {
		try {
			Customer customer = customerService.getCustomerWithEmail(email);
			return ResponseEntity.ok().body(customer);
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("Customer with email : "+email +" is not found");
		}
	}
	
	@GetMapping(value="/findbysamecountry")
	public ResponseEntity<?> getCustomerWithSameCountry(@RequestParam("countryname") String countryname) {
		try {
			List<Customer> customerList = customerService.getCustomerWithSameCountry(countryname);
			return ResponseEntity.ok().body(customerList);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("No customers present with country name : "+countryname);
		}
	}
	
	@PutMapping(value="/update/{customerid}")
	public ResponseEntity<?> updateCustomer(@PathVariable("customerid") Long customerId, @RequestBody CustomerRequest newCustomerRequest) {
		try {
			Customer updatedCustomer = customerService.updateCustomer(customerId, newCustomerRequest);
			return ResponseEntity.ok().body(updatedCustomer);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Update operation failed!");
		}
	}
	
	@GetMapping(value="/count")
	public ResponseEntity<?> countCustomers() {
		try {
			long totalcustomer = customerService.countCustomers();
			return ResponseEntity.ok().body("The total number of customers present in database are "+totalcustomer);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("No customer present");
		}
	}
	
}
