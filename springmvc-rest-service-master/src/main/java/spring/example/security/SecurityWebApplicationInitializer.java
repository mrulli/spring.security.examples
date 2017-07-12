package spring.example.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * The MessageSecurityWebApplicationInitializer will automatically register 
 * the springSecurityFilterChain Filter for every URL in your application to 
 * require authentication.
 * See http://docs.spring.io/spring-security/site/docs/current/guides/html5/hellomvc-javaconfig.html#registering-spring-security-with-the-war
 * 
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}
