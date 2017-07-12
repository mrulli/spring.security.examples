package spring.jwt.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OAuthFlowTest {

	protected URI tokenUri;

	@Before
	public void setUp() {
		tokenUri = URI.create("http://localhost:8080/login");
	}

	/**
	 * No Basic authentication provided, only the hard coded client_id.
	 */
	@Test
	public void testHardCodedAuthenticationFineClient() {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		AccountCredentials credential = new AccountCredentials("admin","password");
		RequestEntity<AccountCredentials> req = new RequestEntity<AccountCredentials>(credential,
				headers, HttpMethod.POST, tokenUri);

		ResponseEntity<Map> response = restTemplate.exchange(req, Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String,List<String>> header = response.getHeaders();
		String accessToken = header.get("Authorization").get(0);
		assertNotNull(accessToken);
	}

	
}
