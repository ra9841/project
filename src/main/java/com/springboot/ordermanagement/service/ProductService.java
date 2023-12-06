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

import com.springboot.ordermanagement.dao.ProductDao;
import com.springboot.ordermanagement.model.Product;
import com.springboot.ordermanagement.request.ProductRequest;

@Service // this tells this class contains business logic
public class ProductService {

	Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductDao productDao;

	public Product saveProduct(ProductRequest productRequest) {
		// take all the request fields from productRequest and set it in product(model)
		// object
		// then save product model object into database and return it
		logger.info("saveProduct method started");
		Product product = new Product();
		product.setProductName(productRequest.getProductName());
		product.setProductQuantity(productRequest.getProductQuantity());
		product.setPrice(productRequest.getPrice());
		product.setDescription(productRequest.getDescription());

		product = productDao.save(product);
		logger.info("saved succesfully");
		if (product == null) {
			logger.info("product not saved excecption occured");
			throw new RuntimeException("Product not saved!");
		}
		return product;
	}

	public Product getProductById(Long productId) {
		logger.info("getProductById method has started");
		// Optional - used to check value can be present or not present
		Optional<Product> productOptional = productDao.findById(productId);
		if (!productOptional.isPresent()) {
			logger.error("no product found with given id");
			throw new RuntimeException("Product not found in database!");
		}
		Product product = productOptional.get();
		logger.info("getProductById method has ended");
		return product;

	}

	public List<Product> getAllProducts() {
		List<Product> productList = productDao.findAll();
		if (productList.isEmpty()) {
			throw new RuntimeException("No product present in database");
		}
		return productList;
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
	public List<Product> getAllProductsWithPagination(Integer pageNumber, Integer pageSize) {

		Page<Product> pageProduct = productDao
				.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("productName").descending()));
		// we need to convert page to list of products and return list as response
		List<Product> productList = new ArrayList<>();
		for (Product cust : pageProduct) {
			productList.add(cust);
		}
		if (productList.isEmpty()) {
			throw new RuntimeException("No product present in database");
		}
		return productList;
	}

	public void deleteProductById(Long productId) {
		productDao.deleteById(productId);
	}

	public Product updateProduct(Long productId, ProductRequest newProductRequest) {
		// 1. get old product details present in database using findbyid
		// 2. remove oldproduct details -> set old product details with
		// newproductrequest
		// 3. save the new product updated details and return it

		Product updatedProduct = null;
		Optional<Product> productOptional = productDao.findById(productId);
		if (productOptional.isPresent()) {
			// oldproduct will have already existing details present in database
			// newproductrequest will have new values which needs to be updated in database
			Product oldProduct = productOptional.get();
			// oldproduct values will be overridden by newcustomwerequest values
			oldProduct.setProductName(newProductRequest.getProductName());
			oldProduct.setProductQuantity(newProductRequest.getProductQuantity());
			oldProduct.setPrice(newProductRequest.getPrice());
			oldProduct.setDescription(newProductRequest.getDescription());
			// oldproduct will now have newupdated values
			updatedProduct = productDao.save(oldProduct);
			if (updatedProduct == null) {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException("Product not found!");
		}

		return updatedProduct;
	}

	public long countProducts() {
		long totalproduct = productDao.count();
		if (totalproduct == 0) {
			throw new RuntimeException();
		}
		return totalproduct;
	}

}
