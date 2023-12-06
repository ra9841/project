package com.springboot.ordermanagement.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.ordermanagement.dao.CustomerDao;
import com.springboot.ordermanagement.model.Customer;
import com.springboot.ordermanagement.request.CustomerRequest;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService;

	@Mock
	private CustomerDao customerDao;

	@BeforeEach // before calling each test case this set up method will call
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	// this is a request payload
	private CustomerRequest getCustomerRequest() {
		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setCustomerName("test");
		customerRequest.setEmail("test123@gmail.com");
		customerRequest.setMobile("+98762543673788");
		customerRequest.setAddress("65, siiaj, djick, siksi");
		customerRequest.setPassword("test123");
		return customerRequest;
	}

	// this is a response object
	private Customer getCustomer() {
		Customer customer = new Customer();
		customer.setCustomerId(123L);
		customer.setCustomerName("test");
		customer.setEmail("test123@gmail.com");
		customer.setMobile("+98762543673788");
		customer.setAddress("65, siiaj, djick, siksi");
		customer.setPassword("test123");
		return customer;
	}
	
	@Test
	public void saveCustomerTestPositive() {
		//customerDao.save(customer);
		Mockito.when(customerDao.save(Mockito.any())).thenReturn(getCustomer());
		Customer actualResponse = customerService.saveCustomer(getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse);
	}
	
	@Test
	public void saveCustomerTestNegative() {
		//customerDao.save(customer);
		Mockito.when(customerDao.save(Mockito.any())).thenReturn(null);
		assertThrows(RuntimeException.class, ()-> customerService.saveCustomer(getCustomerRequest()));
	}
	
	@Test
	public void getCustomerByIdTestPositive() {
		//customerDao.findById(customerId);
		Mockito.when(customerDao.findById(Mockito.any())).thenReturn(Optional.of(getCustomer()));
		Customer actualResponse = customerService.getCustomerById(123L);
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse);
	}
	
	@Test
	public void getCustomerByIdTestNegative() {
		Mockito.when(customerDao.findById(Mockito.any())).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, ()-> customerService.getCustomerById(234L));
	}
	
	@Test
	public void getAllCustomersTestPositive() {
		List<Customer> customersList =new ArrayList<>();
		customersList.add(getCustomer());
		//customerDao.findAll();
		Mockito.when(customerDao.findAll()).thenReturn(customersList);
		List<Customer> actualcustomerList = customerService.getAllCustomers();
		assertNotNull(actualcustomerList);
		assertEquals(customersList, actualcustomerList);
	}
	
	@Test
	public void getAllCustomersTestNegative() {
		List<Customer> customersList =new ArrayList<>();
		Mockito.when(customerDao.findAll()).thenReturn(customersList);
		assertThrows(RuntimeException.class, ()-> customerService.getAllCustomers());
	}
	
	@Test
	public void getAllCustomersWithPaginationTestPositive() {
		List<Customer> customersList =new ArrayList<>();
		customersList.add(getCustomer());
		Page<Customer> pageCustomer = new PageImpl<Customer>(customersList);
		//customerDao.findAll(PageRequest.of(pageNumber, pageSize,Sort.by("customerName").descending()));
		Mockito.when(customerDao.findAll(PageRequest.of(0, 1,Sort.by("customerName").descending()))).thenReturn(pageCustomer);
		List<Customer> actualCustomerList = customerService.getAllCustomersWithPagination(0, 1);
		assertNotNull(actualCustomerList);
		assertEquals(customersList, actualCustomerList);
	}
	
	
	@Test
	public void getAllCustomersWithPaginationTestNegative() {
		List<Customer> customersList =new ArrayList<>();
		Page<Customer> pageCustomer = new PageImpl<Customer>(customersList);
		//customerDao.findAll(PageRequest.of(pageNumber, pageSize,Sort.by("customerName").descending()));
		Mockito.when(customerDao.findAll(PageRequest.of(0, 1,Sort.by("customerName").descending()))).thenReturn(pageCustomer);
		assertThrows(RuntimeException.class, ()-> customerService.getAllCustomersWithPagination(0, 1));
	}
	
	@Test
	public void deleteCustomerByIdTest() {
		customerService.deleteCustomerById(123L);
	}
	
	@Test
	public void getCustomerWithEmailTestPositive() {
		//customerDao.findCustomerByEmail(email);
		Mockito.when(customerDao.findCustomerByEmail(Mockito.any())).thenReturn(getCustomer());
		Customer actualResponse = customerService.getCustomerWithEmail("test@gmail.com");
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse);
	}
	
	@Test
	public void getCustomerWithEmailTestNegative() {
		//customerDao.findCustomerByEmail(email);
		Mockito.when(customerDao.findCustomerByEmail(Mockito.any())).thenReturn(null);
		assertThrows(RuntimeException.class, ()->customerService.getCustomerWithEmail("test@gmail.com"));
	}
	
	@Test
	public void getCustomerWithSameCountryTestPositive() {
		List<Customer> customersList =new ArrayList<>();
		customersList.add(getCustomer());
		//customerDao.findCustomerWithSameCountry(countryname);
		Mockito.when(customerDao.findCustomerWithSameCountry(Mockito.any())).thenReturn(customersList);
		List<Customer> actualResponseList = customerService.getCustomerWithSameCountry(Mockito.any());
		assertNotNull(actualResponseList);
		assertEquals(customersList, actualResponseList);
	}
	
	@Test
	public void getCustomerWithSameCountryTestNegative() {
		List<Customer> customersList =new ArrayList<>();
		//customerDao.findCustomerWithSameCountry(countryname);
		Mockito.when(customerDao.findCustomerWithSameCountry(Mockito.any())).thenReturn(customersList);
		assertThrows(RuntimeException.class, ()-> customerService.getCustomerWithSameCountry(Mockito.any()));
	}
	
	@Test
	public void countCustomersTestPositive() {
		Mockito.when(customerDao.count()).thenReturn(3L);
		long actualCount = customerService.countCustomers();
		assertNotNull(actualCount);
		assertEquals(3L, actualCount);
	}
	
	@Test
	public void countCustomersTestNegative() {
		Mockito.when(customerDao.count()).thenReturn(0L);
		assertThrows(RuntimeException.class, ()-> customerService.countCustomers());
		
	}
	
	@Test
	public void updateCustomerTestPositive() {
		Mockito.when(customerDao.findById(Mockito.any())).thenReturn(Optional.of(getCustomer()));
		Mockito.when(customerDao.save(Mockito.any())).thenReturn(getCustomer());
		Customer actualResponse = customerService.updateCustomer(123L, getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse);
	}
	
	@Test
	public void updateCustomerTestNegative1() {
		Mockito.when(customerDao.findById(Mockito.any())).thenReturn(Optional.of(getCustomer()));
		Mockito.when(customerDao.save(Mockito.any())).thenReturn(null);
		assertThrows(RuntimeException.class, ()-> customerService.updateCustomer(123L, getCustomerRequest()));
		
	}
	
	@Test
	public void updateCustomerTestNegative2() {
		Mockito.when(customerDao.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(customerDao.save(Mockito.any())).thenReturn(null);
		assertThrows(RuntimeException.class, ()-> customerService.updateCustomer(123L, getCustomerRequest()));
		
	}
}
