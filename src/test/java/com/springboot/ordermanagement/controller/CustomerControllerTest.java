package com.springboot.ordermanagement.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.ordermanagement.model.Customer;
import com.springboot.ordermanagement.request.CustomerRequest;
import com.springboot.ordermanagement.service.CustomerService;

@RunWith(SpringRunner.class)
public class CustomerControllerTest {

	@InjectMocks
	private CustomerController customerController;
	
	@Mock
	private CustomerService customerService;
	
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
	public void createCustomerTestPositive() {
		//We need to see if we are calling any other class methods inside actual method
		// if we are calling we need use
		// Mockito.when().thenreturn();
		// Mockito.any() - will take any parameter at runtime and this can be called only inside Mockito.when()
		Mockito.when(customerService.saveCustomer(Mockito.any())).thenReturn(getCustomer());
		//Customer savedCustomer = customerService.saveCustomer(customerRequest);
		//call the actual method which you want to test by passing actual parameter
		ResponseEntity<?> actualResponse = customerController.createCustomer(getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
	}
	
	@Test
	public void createCustomerTestNegative() {
		//We need to see if we are calling any other class methods inside actual method
		// if we are calling we need use
		// Mockito.when().thenthrow();
		// Mockito.any() - will take any parameter at runtime and this can be called only inside Mockito.when()
		Mockito.when(customerService.saveCustomer(Mockito.any())).thenThrow(new RuntimeException());
		//Customer savedCustomer = customerService.saveCustomer(customerRequest);
		//call the actual method which you want to test by passing actual parameter
		ResponseEntity<?> actualResponse = customerController.createCustomer(getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
	}
	
	@Test
	public void getCustomerByIdTestPositive() {
		Mockito.when(customerService.getCustomerById(Mockito.any())).thenReturn(getCustomer());
		//Customer customer = customerService.getCustomerById(customerId);
		ResponseEntity<?> actualResponse =customerController.getCustomerById(123L);
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse.getBody());
	}
	
	@Test
	public void getCustomerByIdTestNegative() {
		Mockito.when(customerService.getCustomerById(Mockito.any())).thenThrow(new RuntimeException());
		//Customer customer = customerService.getCustomerById(customerId);
		ResponseEntity<?> actualResponse =customerController.getCustomerById(123L);
		assertNotNull(actualResponse);
		assertEquals("Customer trying to find is not found inside database!", actualResponse.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
	}
	
	@Test
	public void getAllCustomerTestPositive() {
		List<Customer> customerList = new ArrayList<>();
		customerList.add(getCustomer());
		Mockito.when(customerService.getAllCustomers()).thenReturn(customerList);
		//List<Customer> custList = customerService.getAllCustomers();
		ResponseEntity<?> actualResponse = customerController.getAllCustomer();
		assertNotNull(actualResponse);
		assertEquals(customerList, actualResponse.getBody());
	}
	
	@Test
	public void getAllCustomerTestNegative() {
		Mockito.when(customerService.getAllCustomers()).thenThrow(new RuntimeException());
		//List<Customer> custList = customerService.getAllCustomers();
		ResponseEntity<?> actualResponse = customerController.getAllCustomer();
		assertNotNull(actualResponse);
		assertEquals("No customers found!", actualResponse.getBody());
	}
	
	@Test
	public void getAllCustomerWithPaginationTestPositive() {
		List<Customer> customerList = new ArrayList<>();
		customerList.add(getCustomer());
		Mockito.when(customerService.getAllCustomersWithPagination(Mockito.any(),Mockito.any())).thenReturn(customerList);
		//List<Customer> customerList = customerService.getAllCustomersWithPagination(pageNumber, pageSize);
		ResponseEntity<?> actualResponse = customerController.getAllCustomerWithPagination(1,0);
		assertNotNull(actualResponse);
		assertEquals(customerList, actualResponse.getBody());
	}
	
	@Test
	public void getAllCustomerWithPaginationTestNegative() {
		Mockito.when(customerService.getAllCustomersWithPagination(Mockito.any(),Mockito.any())).thenThrow(new RuntimeException());
		//List<Customer> customerList = customerService.getAllCustomersWithPagination(pageNumber, pageSize);
		ResponseEntity<?> actualResponse = customerController.getAllCustomerWithPagination(1,0);
		assertNotNull(actualResponse);
		assertEquals("No customers found!", actualResponse.getBody());
	}
	
	@Test
	public void deleteCustomerByIdTestPositive() {
		//customerService.deleteCustomerById(customerId);
		// we cannot mockito.when for this other class method - it is not returning any type
		ResponseEntity<?> actualResponse = customerController.deleteCustomerById(123L);
		assertNotNull(actualResponse);
		assertEquals("The customer with customerId "+123L+" is deleted successfully!", actualResponse.getBody());
	}
	
	@Test
	public void getCustomerWithEmailTestPositive() {
		Mockito.when(customerService.getCustomerWithEmail(Mockito.any())).thenReturn(getCustomer());
		//Customer customer = customerService.getCustomerWithEmail(email);
		ResponseEntity<?> actualResponse =customerController.getCustomerWithEmail("test@gmail.com");
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse.getBody());
	}
	
	@Test
	public void getCustomerWithEmailTestNegative() {
		Mockito.when(customerService.getCustomerWithEmail(Mockito.any())).thenThrow(new RuntimeException());
		//Customer customer = customerService.getCustomerWithEmail(email);
		ResponseEntity<?> actualResponse =customerController.getCustomerWithEmail("test123@gmail.com");
		assertNotNull(actualResponse);
		assertEquals("Customer with email : test123@gmail.com is not found", actualResponse.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
	}
	
	@Test
	public void getCustomerWithSameCountryTestPositive() {
		List<Customer> customerList = new ArrayList<>();
		customerList.add(getCustomer());
		Mockito.when(customerService.getCustomerWithSameCountry(Mockito.any())).thenReturn(customerList);
		//List<Customer> customerList = customerService.getCustomerWithSameCountry(countryname);
		ResponseEntity<?> actualResponse = customerController.getCustomerWithSameCountry("siksi");
		assertNotNull(actualResponse);
		assertEquals(customerList, actualResponse.getBody());
	}
	
	@Test
	public void getCustomerWithSameCountryTestNegative() {
		Mockito.when(customerService.getCustomerWithSameCountry(Mockito.any())).thenThrow(new RuntimeException());
		//List<Customer> customerList = customerService.getCustomerWithSameCountry(countryname);
		ResponseEntity<?> actualResponse = customerController.getCustomerWithSameCountry("siksi");
		assertNotNull(actualResponse);
		assertEquals("No customers present with country name : siksi", actualResponse.getBody());
	}
	
	@Test
	public void countCustomersTestPositive() {
		Mockito.when(customerService.countCustomers()).thenReturn(1L);
		//long totalcustomer = customerService.countCustomers();
		ResponseEntity<?> actualResponse = customerController.countCustomers();
		assertNotNull(actualResponse);
		assertEquals("The total number of customers present in database are "+1L, actualResponse.getBody());
	}
	
	@Test
	public void countCustomersTestNegative() {
		Mockito.when(customerService.countCustomers()).thenThrow(new RuntimeException());
		//long totalcustomer = customerService.countCustomers();
		ResponseEntity<?> actualResponse = customerController.countCustomers();
		assertNotNull(actualResponse);
		assertEquals("No customer present", actualResponse.getBody());
	}
	
	@Test
	public void updateCustomerTestPositive() {
		Mockito.when(customerService.updateCustomer(Mockito.any(),Mockito.any())).thenReturn(getCustomer());
		//Customer updatedCustomer = customerService.updateCustomer(customerId, newCustomerRequest);
		ResponseEntity<?> actualResponse =customerController.updateCustomer(123L, getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals(getCustomer(), actualResponse.getBody());
	}
	
	@Test
	public void updateCustomerTestNegative() {
		Mockito.when(customerService.updateCustomer(Mockito.any(),Mockito.any())).thenThrow(new RuntimeException());
		//Customer updatedCustomer = customerService.updateCustomer(customerId, newCustomerRequest);
		ResponseEntity<?> actualResponse =customerController.updateCustomer(123L, getCustomerRequest());
		assertNotNull(actualResponse);
		assertEquals("Update operation failed!", actualResponse.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
	}
	
}
