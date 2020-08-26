### About this project

- This is a simple spring-boot project.


### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.


### Changelog

- Added spring-boot-devtools
- Refactored EmployeeController.java, EmployeeService.java
- Added basic logging
- Validation to entity class
- Added Exception handling
- Added initial data loading into h2 db
- Added Unit test cases

### Future scope

- Hateoas implementation
- Logging into File (RollingFileAppender)