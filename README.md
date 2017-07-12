### authorization_code flow in spring boot
The authorization code flow involve the following projects:

* **spring-client-app**: emulates the client providing a callback to receive the authorization code;* **spring-oauth2**: the authorization server that provides support for OAuth2 flows and generates auth codes along with access tokens;* **spring-oauth2-resource-provider**: provides a trivial resource server that publish a REST API protected with the OAuth filter

Using these three projects it is possible to either test the Resource Owner Password Credentials (grant type "password") or the more complex authorization_code grant flow.

#### Resource Owner Password Credentials
Get the token with:

```
$ curl -XPOST -k foo:foosecret@localhost:9000/hascode/oauth/token  \
    -d grant_type=password \
    -d client_id=foo \
    -d client_secret=abc123 \
    -d redirect_uri=http://www.hascode.com \
    -d username=bar -d password=barsecret

----
Response:
{
    "access_token":"2519f2bf-18a9-4b31-8480-13459c2adc80",
    "token_type":"bearer",
    "refresh_token":"8ea8b948-8597-4fe3-b9ba-8261aff021ba",
    "expires_in":42754,
    "scope":"openid"
}
```

#### authorization_code grant flow
Trigger the following request from a web browser:

```
http://localhost:9000/hascode/oauth/authorize?client_id=foo&response_type=code&redirect_uri=http%3A//localhost%3A8080/client-app/callback&state=123456
```

Follow the stream and once you get the authorization code invoke the following POST

```
curl -u "foo:foosecret" \
   -X POST 'http://localhost:9000/hascode/oauth/token?grant_type=authorization_code&code=TQ2p9f&redirect_uri=http%3A//localhost%3A8080/client-app/callback'

----
Response:
{
    "access_token":"2519f2bf-18a9-4b31-8480-13459c2adc80",
    "token_type":"bearer",
    "refresh_token":"8ea8b948-8597-4fe3-b9ba-8261aff021ba",
    "expires_in":42754,
    "scope":"openid"
}
```

### rest-service-with-oauth
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

### spring.jwt.example

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









