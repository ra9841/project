package com.springboot.ordermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.ordermanagement.model.Product;

@Repository // this tells the it does database operations
public interface ProductDao extends JpaRepository<Product, Long>{
	
}
