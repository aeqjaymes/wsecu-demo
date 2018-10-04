# WSECU Tech Challenge
Jaymes Bearden [<jaymes.bearden@aequilibrium.ca>](mailto:jaymes.bearden@aequilibrium.ca)


## Installation
With the provided Maven, a simple `mvnw clean compile` will download and compile this application.


## Running
After installation, this application can be executed with `mvnw spring-boot:run`. After the application has launched,
it can be accessed via `localhost:8080`.


## Assumptions
- Only partial functionality has been supplied to demonstrate various engineering techniques
- This project assumes a standard deployment of a Spring Boot application (eg, packaged and executed as a jar)
- The style organization has been selected dividing a project's functionality by type instead of components 
(eg, controllers, services, data vs `.../user/UserController,UserService,UserEntity,etc`)
- Tests coverage is partial to demonstrate various techniques
- Security, caching, session management, load balancing, health monitoring, connection pooling, etc is ignored


## Documentation
Once the application has been launched, Swagger documentation can be accessed via `http://localhost:8080/swagger-ui.html`. Any
of the supplied endpoints can be tested via the standard Swagger UI functionality.

Note: Various project components also have varying degrees of documentation.


## Testing
This project can be tested via `mvnw clean test`.

Also provided is a Postman example collection available at `<root>/postman_collection.json` demonstrating various endpoint calls.
The collection ordered top to bottom for suggested execution (as some of the update requests are dependent on previous endpoint calls).
