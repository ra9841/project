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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ordermanagement.model.Order;
import com.springboot.ordermanagement.request.OrderRequest;
import com.springboot.ordermanagement.response.OrderResponse;
import com.springboot.ordermanagement.service.OrderService;

@RestController
@RequestMapping(value="/om/v1/order")
public class OrderController {

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
	
	Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	// @RequestBody - it takes the input from front end in json format and passes it to API
	@PostMapping(value="/save") // other way - @RequestMapping(value="/save", method=RequestMethod.POST)
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
		logger.info("createOrder API has started");
		OrderResponse orderResponse = new OrderResponse();
		try {
			Order savedOrder = orderService.saveOrder(orderRequest);
			logger.info("order saved successfully");
			orderResponse.setData(savedOrder);
			orderResponse.setMessage("Order Saved successsfully into database!");
			orderResponse.setCode(HttpStatus.OK.toString());
			logger.info("createOrder API ended");
			return ResponseEntity.ok().body(orderResponse);
		} catch (Exception e) {
			logger.error("order cannot be saved, an exception occured : "+e.getMessage());
			orderResponse.setData(null);
			orderResponse.setMessage("Order could not be saved!");
			orderResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			return ResponseEntity.internalServerError().body(orderResponse);
		}	
	}
	
	//@PathVariable - value comes within url
	@GetMapping(value="/find/{orderid}")
	public ResponseEntity<?> getOrderById(@PathVariable("orderid") Long orderId) {
		logger.info("getOrderById API has started");
		try {
			Order order = orderService.getOrderById(orderId);
			logger.info("order fetched successfully and getOrderById API has ended");
			return ResponseEntity.ok().body(order);
		}catch (Exception e) {
			logger.error("order did not found");
			return ResponseEntity.internalServerError().body("Order trying to find is not found inside database!");
		}
	}
	
	@GetMapping(value="/findall")
	public ResponseEntity<?> getAllOrder() {
		try {
			List<Order> custList = orderService.getAllOrders();
			return ResponseEntity.ok().body(custList);
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("No orders found!");
		}
	}
	
	@GetMapping(value="/findallwithpage")
	public ResponseEntity<?> getAllOrderWithPagination(@RequestParam("pagesize") Integer pageSize, @RequestParam("pageno") Integer pageNumber) {
		try {
			List<Order> orderList = orderService.getAllOrdersWithPagination(pageNumber, pageSize);
			return ResponseEntity.ok().body(orderList);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body("No orders found!");
		}
	}
	
	@DeleteMapping(value="/delete/{orderid}")
	public ResponseEntity<?> deleteOrderById(@PathVariable("orderid") Long orderId) {
		try {
			orderService.deleteOrderById(orderId);
			return ResponseEntity.ok().body("The order with orderId "+orderId+" is deleted successfully!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("The order with orderId "+orderId+" is not found!");
		}
	}
	
	@PutMapping(value="/update/{orderid}")
	public ResponseEntity<?> updateOrder(@PathVariable("orderid") Long orderId, @RequestBody OrderRequest newOrderRequest) {
		try {
			Order updatedOrder = orderService.updateOrder(orderId, newOrderRequest);
			return ResponseEntity.ok().body(updatedOrder);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Update operation failed!");
		}
	}
	
	@GetMapping(value="/count")
	public ResponseEntity<?> countOrders() {
		try {
			long totalorder = orderService.countOrders();
			return ResponseEntity.ok().body("The total number of orders present in database are "+totalorder);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("No order present");
		}
	}
	
}
