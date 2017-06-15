# springmvc-rest-service
## Create a simple Hello World REST Web Service
Publish a RESTful Web Service where the following Web Methods are available:
* ```http://<webserver_add>:<webserver_port>/greeting``` (e.g., http://localhost:8080/greeting).
* ```http://<webserver_add>:<webserver_port>/greeting?name=<your_name>``` (e.g., http://localhost:8080/greeting?name=Luca)

The output of the calls has to be:

* ```{"id":22,"content":"Hello, World!"}```
* ```{"id":23,"content":"Hello, Luca!"}```
respectively. Please note that the attribute "id" has to be incremented at each call.

Export a war and install it on Tomcat. Try remote debug.

## Hints:
Publish a RESTful Web Service.
* Use the wizard "Spring Starter Project" available in Eclipse STS.
* Create a POJO to create the response and to maintain the status (i.e., the incremental "id" attribute. Have a look at ```incrementAndGet#AtomicLong```).
* Create the Web Service using the Spring Annotations. Use ```@RestController```, ```@RequestMapping```, ```@PathVariable``` and ```@RequestParam``` to create the Web Service and the Web Method.

To enable remote debug on Tomcat please add to the bin dir the file ```setenv.sh/bat``` with the following content:
* ```CATALINA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"```
Then use the "Remote Java Application" Debug configuration to attach to Tomcat.
