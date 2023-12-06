package com.springboot.ordermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.ordermanagement.dao.CustomerDao;
import com.springboot.ordermanagement.dao.OrderDao;
import com.springboot.ordermanagement.dao.ProductDao;
import com.springboot.ordermanagement.enums.OrderStatus;
import com.springboot.ordermanagement.enums.PaymentMethod;
import com.springboot.ordermanagement.model.Customer;
import com.springboot.ordermanagement.model.Order;
import com.springboot.ordermanagement.model.Product;
import com.springboot.ordermanagement.request.OrderRequest;

@Service // this tells this class contains business logic
public class OrderService {

	Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;

	public Order saveOrder(OrderRequest orderRequest) {
		// take all the request fields from orderRequest and set it in order(model)
		// object
		// then save order model object into database and return it
		logger.info("saveOrder method started");
		Order order = new Order();
		order.setProductPurchaseQuantity(orderRequest.getProductPurchaseQuantity());
		//order.setTotalPrice(orderRequest.getTotalPrice());
		//order.setOrderStatus(orderRequest.getOrderStatus());
		
		order.setOrderStatus(OrderStatus.IN_PROGRESS.toString());
		
		order.setDeliveryAddress(orderRequest.getDeliveryAddress());
		//order.setPaymentMethod(orderRequest.getPaymentMethod());
		
		if(PaymentMethod.ONLINE_TRANSACTION.toString().equalsIgnoreCase(orderRequest.getPaymentMethod())) {
			order.setPaymentMethod(PaymentMethod.ONLINE_TRANSACTION.toString());
		} else if(PaymentMethod.CREDIT_CARD.toString().equalsIgnoreCase(orderRequest.getPaymentMethod())) {
			order.setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());
		} else if(PaymentMethod.DEBIT_CARD.toString().equalsIgnoreCase(orderRequest.getPaymentMethod())) {
			order.setPaymentMethod(PaymentMethod.DEBIT_CARD.toString());
		} else {
			order.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY.toString());
		}

		Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
		order.setCustomer(customer);
		
		Product product = productService.getProductById(orderRequest.getProductId());
		order.setProduct(product);
		
		// get the price of the product and convert it to integer
		int productPrice = Integer.parseInt(product.getPrice());
		// convert the productpurchasequantity to integer
		int prodPurshQnty = Integer.parseInt(orderRequest.getProductPurchaseQuantity());
		// now calculate totalprice and set it
		int totalPrice = productPrice*prodPurshQnty;
		order.setTotalPrice(String.valueOf(totalPrice));
		
		order = orderDao.save(order);
		logger.info("saved succesfully");
		if (order == null) {
			logger.info("order not saved excecption occured");
			throw new RuntimeException("Order not saved!");
		}
		return order;
	}

	public Order getOrderById(Long orderId) {
		logger.info("getOrderById method has started");
		// Optional - used to check value can be present or not present
		Optional<Order> orderOptional = orderDao.findById(orderId);
		if (!orderOptional.isPresent()) {
			logger.error("no order found with given id");
			throw new RuntimeException("Order not found in database!");
		}
		Order order = orderOptional.get();
		logger.info("getOrderById method has ended");
		return order;

	}

	public List<Order> getAllOrders() {
		List<Order> orderList = orderDao.findAll();
		if (orderList.isEmpty()) {
			throw new RuntimeException("No order present in database");
		}
		return orderList;
	}

	/*
	 * pagination - fetching the records based on pages two parameters - page
	 * number, page size pagenumber - number of page requested pagesize - how many
	 * records needs to be present in each page
	 * 
	 * pagesize -10, number of records -36 0th page - 1-10 1st page - 11-20 2nd page
	 * - 21-30 3rd page - 31-36
	 * 
	 * pagesize-3, number of records-25 0th page - 1-3 1st page - 4-6 2nd page - 7-9
	 * 3rd page - 10-12 4th page - 13-15 5th page - 16-18 6th page - 19-21 7th page
	 * - 22-24 8th page - 25
	 */
	public List<Order> getAllOrdersWithPagination(Integer pageNumber, Integer pageSize) {

		Page<Order> pageOrder = orderDao
				.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("orderName").descending()));
		// we need to convert page to list of orders and return list as response
		List<Order> orderList = new ArrayList<>();
		for (Order cust : pageOrder) {
			orderList.add(cust);
		}
		if (orderList.isEmpty()) {
			throw new RuntimeException("No order present in database");
		}
		return orderList;
	}

	public void deleteOrderById(Long orderId) {
		orderDao.deleteById(orderId);
	}

	public Order updateOrder(Long orderId, OrderRequest newOrderRequest) {
		// 1. get old order details present in database using findbyid
		// 2. remove oldorder details -> set old order details with
		// neworderrequest
		// 3. save the new order updated details and return it

		Order updatedOrder = null;
		Optional<Order> orderOptional = orderDao.findById(orderId);
		if (orderOptional.isPresent()) {
			// oldorder will have already existing details present in database
			// neworderrequest will have new values which needs to be updated in database
			Order oldOrder = orderOptional.get();
			// oldorder values will be overridden by newcustomwerequest values
			oldOrder.setProductPurchaseQuantity(newOrderRequest.getProductPurchaseQuantity());
			//oldOrder.setOrderStatus(newOrderRequest.getOrderStatus());
			
			if(OrderStatus.SHIPPED.toString().equalsIgnoreCase(newOrderRequest.getOrderStatus())) {
				oldOrder.setOrderStatus(OrderStatus.SHIPPED.toString());
			} else if(OrderStatus.DELIVERED.toString().equalsIgnoreCase(newOrderRequest.getOrderStatus())) {
				oldOrder.setOrderStatus(OrderStatus.DELIVERED.toString());
			} else if(OrderStatus.RETURNED.toString().equalsIgnoreCase(newOrderRequest.getOrderStatus())) {
				oldOrder.setOrderStatus(OrderStatus.RETURNED.toString());
			} else if(OrderStatus.CANCELLED.toString().equalsIgnoreCase(newOrderRequest.getOrderStatus())) {
				oldOrder.setOrderStatus(OrderStatus.CANCELLED.toString());
			} else {
				oldOrder.setOrderStatus(OrderStatus.IN_PROGRESS.toString());
			}
			
			//oldOrder.setPaymentMethod(newOrderRequest.getPaymentMethod());
			if(PaymentMethod.ONLINE_TRANSACTION.toString().equalsIgnoreCase(newOrderRequest.getPaymentMethod())) {
				oldOrder.setPaymentMethod(PaymentMethod.ONLINE_TRANSACTION.toString());
			} else if(PaymentMethod.CREDIT_CARD.toString().equalsIgnoreCase(newOrderRequest.getPaymentMethod())) {
				oldOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());
			} else if(PaymentMethod.DEBIT_CARD.toString().equalsIgnoreCase(newOrderRequest.getPaymentMethod())) {
				oldOrder.setPaymentMethod(PaymentMethod.DEBIT_CARD.toString());
			} else {
				oldOrder.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY.toString());
			}
			
			oldOrder.setDeliveryAddress(newOrderRequest.getDeliveryAddress());
			//oldOrder.setTotalPrice(newOrderRequest.getTotalPrice());
			
			Customer customer = customerService.getCustomerById(newOrderRequest.getCustomerId());
			oldOrder.setCustomer(customer);
			
			Product product = productService.getProductById(newOrderRequest.getProductId());
			oldOrder.setProduct(product);
			
			// get the price of the product and convert it to integer
			int productPrice = Integer.parseInt(product.getPrice());
			// convert the productpurchasequantity to integer
			int prodPurshQnty = Integer.parseInt(newOrderRequest.getProductPurchaseQuantity());
			// now calculate totalprice and set it
			int totalPrice = productPrice*prodPurshQnty;
			oldOrder.setTotalPrice(String.valueOf(totalPrice));
			
			// oldorder will now have newupdated values
			updatedOrder = orderDao.save(oldOrder);
			if (updatedOrder == null) {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException("Order not found!");
		}

		return updatedOrder;
	}

	public long countOrders() {
		long totalorder = orderDao.count();
		if (totalorder == 0) {
			throw new RuntimeException();
		}
		return totalorder;
	}

}
