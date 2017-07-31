package spring.jwt.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The @EnableWebSecurity annotation and WebSecurityConfigurerAdapter work together to provide web based security. 
 * By extending WebSecurityConfigurerAdapter we are able to do the following:
 * - Require the user to be authenticated prior to accessing any URL within our application
 * - Assign users and roles
 * - Enable HTTP Basic and Form based authentication
 * - Automatically create a login and a logout pages
 * In this case we allow free access to endpoint /login and we check the presence of JWT
 * token for the others endpoints.
 * This class is defined in the tutorial: https://auth0.com/blog/securing-spring-boot-with-jwts/
 * See https://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security#websecurityconfigureradapter
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.POST, "/login").permitAll()
        .anyRequest().authenticated()
        .and()
        // We filter the api/login requests
        .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
        // And filter other requests to check the presence of JWT in header
        .addFilterBefore(new JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // Create a default account
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password("password")
        .roles("ADMIN");
  }
}