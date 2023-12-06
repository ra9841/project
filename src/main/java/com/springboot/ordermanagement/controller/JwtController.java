package com.springboot.ordermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ordermanagement.request.JwtRequest;
import com.springboot.ordermanagement.response.JwtResponse;
import com.springboot.ordermanagement.service.JwtUserDetailsService;
import com.springboot.ordermanagement.util.JwtUtil;

@RestController
@CrossOrigin
public class JwtController {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> createToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (Exception e) {
			throw new Exception("Invalid credentials",e);
		}
		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		final String jwtToken = jwtUtil.generateJwtToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(jwtToken));
	}
}
