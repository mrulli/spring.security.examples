package spring.jwt.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple class to run the application with SpringBoot.
 * Simply run this class as a Java application.
 */

@SpringBootApplication
@RestController
@EnableAutoConfiguration
public class DemoApplication {

    @RequestMapping("/")
    String hello() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}