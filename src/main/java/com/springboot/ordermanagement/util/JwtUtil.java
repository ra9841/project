package com.springboot.ordermanagement.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable {

	// defines token expiration time in ms
	private static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

	// take the secret value from application.properties and assign to jwtsecret
	@Value("${secret}")
	private String jwtSecret;

	// this method is responsible for generating jwt token from userdetails
	public String generateJwtToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	// validating token
	// get username from token
	// check expiration of the token
	// validate username and expiration

	// get username from jwt token
	public String getUserNameFromToken(String token) {
		final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();// subject contains username
	}

	// validating the jwt token
	public Boolean validateJwtToken(String token, UserDetails userDetails) {
		// take username from token
		// take username from userdetails
		// match both of them if it's matching token is validated successfully else not
		String usernameFromToken = getUserNameFromToken(token);

		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		Boolean isTokenExpired = claims.getExpiration().before(new Date());

		if (usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired) {
			return true;
		} else {
			return false;
		}
	}
}
