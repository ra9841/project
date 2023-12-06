package com.springboot.ordermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ordermanagement.model.Customer;

@Repository // this tells the it does database operations
public interface CustomerDao extends JpaRepository<Customer, Long>{

	// writing user defined own native query
	@Query(nativeQuery = true, value="select * from customer where email = :email1")
	public Customer findCustomerByEmail(String email1);
	
	// query to find customers whose country are same
	//select * from oms_sep.customer where address like '%eklsmn';

	//query to find customers whose city are same
	//select * from oms_sep.customer where address like '%ejek%';

	//query to find customers whose doornumber are same
	//select * from oms_sep.customer where address like '97%';
	
	@Query(nativeQuery = true, value="select * from customer where address like %:coutryname")
	public List<Customer> findCustomerWithSameCountry(String coutryname);
}
