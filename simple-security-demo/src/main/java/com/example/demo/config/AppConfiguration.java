package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
@EnableWebSecurity
public class AppConfiguration
//extending this is only needed if we customize the PermissionEvaluator:	
		extends GlobalMethodSecurityConfiguration { 
	
	@Autowired
	private ApplicationContext context;
	
	@Bean
	public UserDetailsService userDetailsService() throws Exception {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withUsername("user")
					.password("password")
					.roles("USER").build());
		manager.createUser(
				User.withUsername("admin")
					.password("password")
					.roles("ADMIN").build());
		return manager;
	}
	
	// Setting up the custom PermissionEvaluator:
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
          new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        // This is important to allow other SPEL-based @PreAuthorize annotation to work:
        expressionHandler.setApplicationContext(context);
        return expressionHandler;
    }
}
