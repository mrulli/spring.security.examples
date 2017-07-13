package spring.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Authorization server is the one responsible for verifying credentials and if credentials are OK, providing the tokens. 
 * It also contains information about registered clients and possible access scopes and grant types. 
 * The token store is used to store the token (in memory or on a database as well).
 * The @EnableAuthorizationServer annotation is used to configure the OAuth 2.0 Authorization Server mechanism.
 * See https://projects.spring.io/spring-security-oauth/docs/oauth2.html#authorization-server-configuration
 * Example here: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/ 
 */

@Configuration
@EnableAuthorizationServer 
@ComponentScan(basePackages = { "spring.example.*" })
@PropertySource("classpath:client.properties")
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static String REALM = "MY_OAUTH_REALM";

	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Value("${client.id:client}")
	private String clientId;
	@Value("${client.secret:secret}")
	private String clientSecret;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Example of JDBC client storage: https://goo.gl/W6qTzP
		clients.inMemory()
		.withClient(clientId)
		.secret(clientSecret)
		.authorizedGrantTypes("bearer", "password", "authorization_code", "refresh_token", "implicit")
		.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
		.redirectUris("http://localhost:8080/rest-service-with-oauth/callback")
		.scopes("read", "write", "trust");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
		.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm(REALM + "/client");
	}

}