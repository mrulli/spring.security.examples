package spring.example.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * A Resource Server serves resources that are protected by the OAuth2 token. 
 * @EnableResourceServer annotation, applied on OAuth2 Resource Servers, enables a Spring Security filter that authenticates 
 * requests using an incoming OAuth2 token. 
 * See https://projects.spring.io/spring-security-oauth/docs/oauth2.html#resource-server-configuration
 * Example here: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "my_rest_api";
	private static final String SECURED_REST_API = "/greeting/**";
	private static final String REQUIRED_ROLE =  "ADMIN";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.
		anonymous().disable()
		// filter for incoming requests that must be secured
		.requestMatchers().antMatchers(SECURED_REST_API)
		.and().authorizeRequests()
		 // It is possible to define required role to access a resource. Roles are associated with users in
		 // spring.example.security.OAuth2SecurityConfiguration
		.antMatchers(SECURED_REST_API).access("hasRole('"+ REQUIRED_ROLE + "')")
		.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

}