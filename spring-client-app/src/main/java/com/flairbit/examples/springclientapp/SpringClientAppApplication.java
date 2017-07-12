package com.flairbit.examples.springclientapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Debug authorize endpoint at: org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * Debug token endpoint at: org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 * 
 * Trigger authorization_code OAuth flow with:
	http://localhost:9000/hascode/oauth/authorize?client_id=foo&response_type=code&redirect_uri=http%3A//localhost%3A8080/client-app/callback&state=123456
 */
@SpringBootApplication
@RestController
public class SpringClientAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringClientAppApplication.class, args);
	}
	
	@RequestMapping("/callback")
	public String user(HttpServletRequest request) {
		String authCode = request.getParameter("code");	
		return "Authorization code: " + authCode;
	}
}

