package spring.example.hello;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple REST endpoint to be used as callback URI in authorization code authentication flow.
 */
@RestController
public class CallbackController {
	@RequestMapping("/callback")
	public String user(HttpServletRequest request) {
		String authCode = request.getParameter("code");	
		return "Authorization code: " + authCode;
	}
}
