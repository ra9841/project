package com.springboot.ordermanagement.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.ordermanagement.dao.CustomerDao;
import com.springboot.ordermanagement.model.Customer;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerDao.findCustomerByEmail(username);
		if(username.equals(customer.getEmail())) {
			return new User(customer.getEmail(), new BCryptPasswordEncoder().encode(customer.getPassword()), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("user not found : "+username);
		}
		
	}

}
