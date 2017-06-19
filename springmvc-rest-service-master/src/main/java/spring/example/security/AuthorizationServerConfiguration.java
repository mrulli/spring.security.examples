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

@Configuration
@EnableAuthorizationServer 
@ComponentScan(basePackages = { "spring.example.*" })
@PropertySource("classpath:client.properties")
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
@Value("${client.id:client}")
private String clientId;
@Value("${client.secret:secret}")
private String clientSecret;

  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager);
  }

@Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient(clientId)
        .secret(clientSecret)
        .authorizedGrantTypes("authorization_code", "refresh_token").scopes("openid");
  }

}