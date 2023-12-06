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

import com.springboot.ordermanagement.model.Product;
import com.springboot.ordermanagement.request.ProductRequest;
import com.springboot.ordermanagement.response.ProductResponse;
import com.springboot.ordermanagement.service.ProductService;

@RestController
@RequestMapping(value="/om/v1/product")
public class ProductController {

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
	
	Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	// @RequestBody - it takes the input from front end in json format and passes it to API
	@PostMapping(value="/save") // other way - @RequestMapping(value="/save", method=RequestMethod.POST)
	public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
		logger.info("createProduct API has started");
		ProductResponse productResponse = new ProductResponse();
		try {
			Product savedProduct = productService.saveProduct(productRequest);
			logger.info("product saved successfully");
			productResponse.setData(savedProduct);
			productResponse.setMessage("Product Saved successsfully into database!");
			productResponse.setCode(HttpStatus.OK.toString());
			logger.info("createProduct API ended");
			return ResponseEntity.ok().body(productResponse);
		} catch (Exception e) {
			logger.error("product cannot be saved, an exception occured : "+e.getMessage());
			productResponse.setData(null);
			productResponse.setMessage("Product could not be saved!");
			productResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			return ResponseEntity.internalServerError().body(productResponse);
		}	
	}
	
	//@PathVariable - value comes within url
	@GetMapping(value="/find/{productid}")
	public ResponseEntity<?> getProductById(@PathVariable("productid") Long productId) {
		logger.info("getProductById API has started");
		try {
			Product product = productService.getProductById(productId);
			logger.info("product fetched successfully and getProductById API has ended");
			return ResponseEntity.ok().body(product);
		}catch (Exception e) {
			logger.error("product did not found");
			return ResponseEntity.internalServerError().body("Product trying to find is not found inside database!");
		}
	}
	
	@GetMapping(value="/findall")
	public ResponseEntity<?> getAllProduct() {
		try {
			List<Product> custList = productService.getAllProducts();
			return ResponseEntity.ok().body(custList);
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("No products found!");
		}
	}
	
	@GetMapping(value="/findallwithpage")
	public ResponseEntity<?> getAllProductWithPagination(@RequestParam("pagesize") Integer pageSize, @RequestParam("pageno") Integer pageNumber) {
		try {
			List<Product> productList = productService.getAllProductsWithPagination(pageNumber, pageSize);
			return ResponseEntity.ok().body(productList);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body("No products found!");
		}
	}
	
	@DeleteMapping(value="/delete/{productid}")
	public ResponseEntity<?> deleteProductById(@PathVariable("productid") Long productId) {
		try {
			productService.deleteProductById(productId);
			return ResponseEntity.ok().body("The product with productId "+productId+" is deleted successfully!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("The product with productId "+productId+" is not found!");
		}
	}
	
	@PutMapping(value="/update/{productid}")
	public ResponseEntity<?> updateProduct(@PathVariable("productid") Long productId, @RequestBody ProductRequest newProductRequest) {
		try {
			Product updatedProduct = productService.updateProduct(productId, newProductRequest);
			return ResponseEntity.ok().body(updatedProduct);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Update operation failed!");
		}
	}
	
	@GetMapping(value="/count")
	public ResponseEntity<?> countProducts() {
		try {
			long totalproduct = productService.countProducts();
			return ResponseEntity.ok().body("The total number of products present in database are "+totalproduct);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("No product present");
		}
	}
	
}
