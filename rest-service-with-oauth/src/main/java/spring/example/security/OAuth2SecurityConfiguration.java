package spring.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * The @EnableWebSecurity annotation and WebSecurityConfigurerAdapter work together to provide web based security. 
 * By extending WebSecurityConfigurerAdapter we are able to do the following:
 * - Require the user to be authenticated prior to accessing any URL within our application
 * - Assign users and roles
 * - Enable HTTP Basic and Form based authentication
 * - Automatically create a login and a logout pages
 * See https://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security#websecurityconfigureradapter
 * Example here: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/
 */

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "spring.example.*" })
@PropertySource("classpath:user.properties")
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${user.id:user}")
	private String userId;
	@Value("${user.password:userpsw}")
	private String userPassword;
	@Value("${admin.id:admin}")
	private String adminId;
	@Value("${admin.password:adminpsw}")
	private String adminPassword;
	
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String USER_ROLE = "USER";
	
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		// Example of JDBC authentication: https://goo.gl/NB9zgA
        auth.inMemoryAuthentication()
        // Define user's roles
        .withUser(adminId).password(adminPassword).roles(ADMIN_ROLE).and()
        .withUser(userId).password(userPassword).roles(USER_ROLE);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
}
