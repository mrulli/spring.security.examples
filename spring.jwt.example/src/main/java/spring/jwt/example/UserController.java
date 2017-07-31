package spring.jwt.example;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import spring.jwt.example.model.User;

@RestController
public class UserController {

  /* Maps to all HTTP actions by default (GET,POST,..)*/
  @RequestMapping("/users")
  public @ResponseBody List<User> getUsers() {
    return Arrays.asList(new User("John", "Deer"), new User("David", "Collins"));
  }
}
