package spring.example.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Adding the annotation @EnableWebMvc to an @Configuration class imports the Spring MVC
 * configuration from WebMvcConfigurationSupport.
 * See https://goo.gl/5xupJY
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "spring.example")
public class HelloWorldConfiguration {

}