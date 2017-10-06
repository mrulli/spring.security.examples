# What you can find here

#### **simple-security-demo** 
Contains examples on how to manage authorization with

* ROLE-based authentication
* Custom PermissionEvaluator
* Spring Expression Language

#### OAuth with Spring Boot
* **spring-client-app**
* **spring-oauth2**
* **spring-oauth2-resource-provider**

#### OAuth with Spring MVC
* **rest-service-with-oauth**

#### JWT and Spring
* **spring.jwt.example**

## OAuth with Spring Boot
### Projects description
The authorization code flow involve the following projects:

* **spring-client-app**: emulates the client providing a callback to receive the authorization code;* **spring-oauth2**: the authorization server that provides support for OAuth2 flows and generates auth codes along with access tokens;* **spring-oauth2-resource-provider**: provides a trivial resource server that publish a REST API protected with the OAuth filter

Using these three projects it is possible to either test the Resource Owner Password Credentials (grant type "password") or the more complex authorization_code grant flow.

#### Resource Owner Password Credentials Flow
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

#### `authorization_code` Grant Flow
Trigger the following request from a web browser:


http://localhost:9000/hascode/oauth/authorize?client_id=foo&response_type=code&redirect_uri=http%3A//localhost%3A8080/client-app/callback&state=123456

Follow the stream, login with **bar/barsecret** user credentials and once you get the authorization code invoke the following POST

```
$ curl -u "foo:foosecret" \
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

## OAuth with Spring MVC
This project contains an implementation of the OAuth authorization server on top of Spring MVC. It provides the same features of the SpringBoot-based projects above all packaged in a single Spring MVC war file. Also the client callback REST API is packaged in the same project for convenience.

### rest-service-with-oauth
This project should be run under a Tomcat server and exposes a secured REST endpoint on 

http://localhost:8080/rest-service-with-oauth/greeting

Client and users could be managed in the configuration files: `client.properties` and `user.properties` respectively. 

`OAuthTest` launches a series of unit tests to verify the OAuth flow. It could be run as a JUnit Test.

`GreetingController` is the secured REST service.

`CallbackController` exposes a webservice used as callback in _authorization___code flow_ in order to retrieve the authorization code required to obtain the token.

`ResourceServerConfiguration` manages the Resource Server security and the secured endpoints.

`AuthorizationServerConfiguration` manages the Authorization Server and the allowed clients roles and scopes. In addition in this class are defined the allowed grant types.

`OAuth2SecurityConfiguration` manages users and roles.

#### Resource Owner Password Credentials Flow

Get the token with:

```
$ curl -u "client:clientsecret" \
-X POST 'http://localhost:8080/rest-service-with-oauth/oauth/token?username=admin&password=adminpsw&grant_type=password&client_id=client'

----
Response:

{
	"access_token":"0c0fc427-0e8c-4976-b438-ffb22e00546f",
	"token_type":"bearer",
	"refresh_token":"69820247-cbc2-4476-b01e-e0591b8cd262",
	"expires_in":42725,
	"scope":"read write trust"
}

```

and use it to call the _greeting_ endpoint:

```
$ curl -H "Authorization:Bearer 0c0fc427-0e8c-4976-b438-ffb22e00546f" \
'http://localhost:8080/rest-service-with-oauth/greeting'

----
Response:

{
	"id":1,
	"content":"Hello, World!"
}

```

If you use credentials of user instead of admin's, you receive an error:

```
$ curl -u "client:clientsecret" \
-X POST 'http://localhost:8080/rest-service-with-oauth/oauth/token?username=user&password=userpsw&grant_type=password&client_id=client'

----
Response:

{
	"access_token":"0c0fc427-0e8c-4976-b438-ffb22e00546f",
	"token_type":"bearer",
	"refresh_token":"69820247-cbc2-4476-b01e-e0591b8cd262",
	"expires_in":42134,
	"scope":"read write trust"
}

```
```
$ curl -H "Authorization:Bearer 0c0fc427-0e8c-4976-b438-ffb22e00546f" \
'http://localhost:8080/rest-service-with-oauth/greeting'

----
Response:

{
	"error":"access_denied",
	"error_description":"Access is denied"
}
```
This is because in `ResourceServerConfiguration` the endpoint `greeting` is set to require a role `admin` and in `OAuth2SecurityConfiguration` that role is assigned only to the user with username `admin`.

#### `authorization_code` Grant Flow
Trigger the following request from a web browser:

http://localhost:8080/rest-service-with-oauth/oauth/authorize?client_id=client&response_type=code&redirect_uri=http://localhost:8080/rest-service-with-oauth/callback

Follow the stream and login with admin's credentials (**admin/adminpsw**), once you get the authorization code invoke the following POST using the code:

```
$ curl -u "client:clientsecret" \
-X POST "http://localhost:8080/rest-service-with-oauth/oauth/token?grant_type=authorization_code&code=l4So3I&redirect_uri=http://localhost:8080/rest-service-with-oauth/callback"

----
Response:

{
	"access_token":"f78652ca-185e-4a93-ae5c-fd0e786df44f",
	"token_type":"bearer",
	"refresh_token":"39afd8ab-d525-4112-9b92-0957977d388c",
	"expires_in":41171,
	"scope":"read write trust"
}
```

and use it the access token to call the _greeting_ endpoint:

```
$ curl -H "Authorization:Bearer f78652ca-185e-4a93-ae5c-fd0e786df44f" \
'http://localhost:8080/rest-service-with-oauth/greeting'

----
Response:

{
	"id":1,
	"content":"Hello, World!"
}

```

Note that if you previously login as _user_, you will not be authorized to invoke the endpoint:

```
$ curl -H "Authorization:Bearer 0c0fc427-0e8c-4976-b438-ffb22e00546f" \
'http://localhost:8080/rest-service-with-oauth/greeting'

----
Response:

{
	"error":"access_denied",
	"error_description":"Access is denied"
}

```

## JWT and Spring

### spring.jwt.example

This project exposes a secured REST endpoint on `http://localhost:8080/users`.

Inspired by [this tutorial](https://auth0.com/blog/securing-spring-boot-with-jwts/).

#### Content:

`OAuthFlowTest` contains a simple authentication test.

`UserController` is the secured REST endpoint.

`TokenAuthenticationService` manages the token generation and authentication.

`WebSecurityConfig` manages login endpoint. Permitted users are defined here.

#### Usage:

1. Launch it using Spring Boot, simply run the class `DemoApplication` as Java application.
2. Get a token with:

	```
	$ curl -i -X POST \
	  http://localhost:8080/login \
	  -H 'content-type: application/json' \
	  -d '{
	"username":"admin",
	"password":"password"
	}'
	
	----
	Response:
	
	HTTP/1.1 200 
	X-Content-Type-Options: nosniff
	X-XSS-Protection: 1; mode=block
	Cache-Control: no-cache, no-store, max-age=0, must-revalidate
	Pragma: no-cache
	Expires: 0
	X-Frame-Options: DENY
	Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUwMjM2Njc2NX0.iDDSSgrc3ZGcBB9Z-JCT5wM8pasR5KBWrtaZm9Kuk6uf729w0SBqljYWaRWyHsSfC0zv2JcBg3WiZFGQyQbFRg
	Content-Length: 0
	Date: Mon, 31 Jul 2017 12:06:05 GMT
	
	```

3. Call the `http://localhost:8080/users` using the token:

	```
	curl -X GET \
	  http://localhost:8080/users \
	  -H 'authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUwMjM2Njc2NX0.iDDSSgrc3ZGcBB9Z-JCT5wM8pasR5KBWrtaZm9Kuk6uf729w0SBqljYWaRWyHsSfC0zv2JcBg3WiZFGQyQbFRg'
	
	----
	Response:
	
	[{"firstName":"John","lastName":"Deer"},{"firstName":"David","lastName":"Collins"}]
	```









 