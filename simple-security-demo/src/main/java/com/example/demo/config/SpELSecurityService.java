package com.example.demo.config;

import java.security.Principal;

import javax.annotation.PostConstruct;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * See https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html
 * 
 * @author matteorulli
 *
 */
@Service("securityService")
public class SpELSecurityService {
	
	@PostConstruct
	public void postInit() {
		System.out.println("Post init called");
	}
	
	public boolean canEdit(final Principal auth, final String name) {
        final UserDetails userDetails = (UserDetails)((UsernamePasswordAuthenticationToken)auth).getPrincipal();
        if (userDetails.getUsername().equals(name))
        		return true;
        return false;
    }
}
