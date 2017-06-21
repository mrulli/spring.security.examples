package spring.example;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import spring.example.configuration.HelloWorldConfiguration;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HelloWorldConfiguration.class)
@WebAppConfiguration
public class OAuthTest {
 
    @Autowired
    private WebApplicationContext wac;
 
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
          .addFilter(springSecurityFilterChain).build();
    }
    
    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/greeting")
          .param("name", "Roberto"))
          .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        String accessToken = obtainAccessToken("user", "userpsw");
        mockMvc.perform(get("/greeting")
          .header("Authorization", "Bearer " + accessToken)
          .param("name", "Roberto"))
          .andExpect(status().isForbidden());
    }
    
    @Test
    public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {
        String accessToken = obtainAccessToken("admin", "adminpsw");
     
        ResultActions result 
        = mockMvc.perform(get("/greeting")
          .param("name", "Roberto")
          .header("Authorization", "Bearer " + accessToken)
          .accept("application/json;charset=UTF-8"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        assertEquals("Hello, Roberto!", jsonParser.parseMap(resultString).get("content").toString());
    }
    
    private String obtainAccessToken(String username, String password) throws Exception {
    	  
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "client");
        params.add("username", username);
        params.add("password", password);
     
        ResultActions result 
          = mockMvc.perform(post("/oauth/token")
            .params(params)
            .with(httpBasic("client","clientsecret"))
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
     
        String resultString = result.andReturn().getResponse().getContentAsString();
     
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
