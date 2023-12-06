package com.springboot.ordermanagement.request;

import lombok.Data;

@Data
public class JwtRequest {

	private String username;// username is nothing but email
	private String password;
}
