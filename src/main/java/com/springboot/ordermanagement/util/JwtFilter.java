package com.springboot.ordermanagement.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.ordermanagement.service.JwtUserDetailsService;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		// Authorization : Bearer cbsijsiois-wn2203ip-snjsikk -> this is passed inside header info
		
		String tokenHeader = request.getHeader("Authorization");// Bearer cbsijsiois-wn2203ip-snjsikk
		String tokenusername = null;
		String token = null;
		
		if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
			token = tokenHeader.substring(7);
			try {
				tokenusername = jwtUtil.getUserNameFromToken(token);
			}catch (Exception e) {
				System.out.println("Unable to get username from token"+e.getMessage());
			}
		} else {
			System.out.println("Bearer token is not present or tokenHeader might be empty");
		}
		// here tokenusername is username coming from token
		if(tokenusername!=null) {
			// tokenusername needs to be checked whether it is present in database or not
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(tokenusername);
			if(jwtUtil.validateJwtToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
