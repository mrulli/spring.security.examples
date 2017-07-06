###rest-service-with-oauth
This project should be run under a Tomcat server and exposes a secured REST endpoint on ```http://localhost:8080/rest-service-with-oauth/greeting```.

Client and users could be managed in the configuration files: ```client.properties``` and ```user.properties``` respectively. 

```OAuthTest``` launches a series of unit tests to verify the OAuth flow. It could be run as a JUnit Test.

```GreetingController``` is the secured REST service.

```ResourceServerConfiguration``` manages the Resource Server security and the secured endpoints.

```AuthorizationServerConfiguration``` manages the Authorization Server and the allowed clients roles and scopes. In addition in this class are defined the allowed grant types.

```OAuth2SecurityConfiguration``` manages users and roles.

Endpoint to obtain an authorization token is:

```http://localhost:8080/rest-service-with-oauth/oauth/token```


Example of request for the _password_ flow:

```
POST /rest-service-with-oauth/oauth/token HTTP/1.1
Host: localhost:8080
Authorization: Basic Y2xpZW50OmNsaWVudHNlY3JldA==
Content-Type: application/x-www-form-urlencoded

username=user&password=userpsw&grant_type=password&client_id=client
```
Note: Authorization header is composed by the client credentials encoded using Basic Auth.

###spring.jwt.example

This project exposes a secured REST endpoint on ```http://localhost:8080/users```.

It could be launched using Spring Boot, simply run the class ```DemoApplication``` as Java application.

```OAuthFlowTest``` contains a simple authentication test.

Authentication is done calling the endpoint:

```http://localhost:8080/login```

Example of request:

```
POST /login HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
"username":"admin",
"password":"password"
}
```

```UserController``` is the secured REST endpoint.

```AuthorizationServerConfiguration``` manages the Authorization Server and the allowed clients roles and scopes. In addition in this class are defined the allowed grant types.

```ResourceServerConfiguration``` manages the Resource Server security and the secured endpoints.

```TokenAuthenticationService``` manages the token generation and authentication.

```WebSecurityConfig``` manages login endpoint.









