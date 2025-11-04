# Lab Class 3 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [What is Spring Boot?](#31-what-is-spring-boot)
2. [Getting started tutorial: REST web services](#32-getting-started-tutorial-rest-web-services)
3. [User management example](#33-user-management-example)
4. [Meals booking](#34-meals-booking)

---

## 3.1 What is Spring Boot?

### Notes

Spring Boot is a framework built on top of the Spring ecosystem that makes it easier to create standalone, production-ready Java applications. Instead of requiring complex setup and configuration, it follows a convention-over-configuration approach: it provides starter dependencies and sensible defaults so developers can focus on business logic rather than boilerplate code.

Enterprise applications are large-scale systems designed to support the operations of organizations. They usually require scalability, security, reliability, integration with databases or external systems, and long-term maintainability. Examples include banking systems, e-commerce platforms, or healthcare management systems.

In the Java world, the leading frameworks for building enterprise applications include:

- Jakarta EE (formerly Java EE), the official set of specifications for enterprise Java applications, with APIs such as JPA, JAX-RS, and JMS.
- Spring and Spring Boot, which provide a more developer-friendly, modular, and flexible alternative.
- Newer options like Quarkus, Micronaut, and MicroProfile, optimized for cloud-native microservices.

Spring Boot and its relationship with Spring and Jakarta:

- The Spring Framework provides the foundation — features such as dependency injection, MVC, and data access.
- Spring Boot builds on this foundation, adding auto-configuration, starter modules, and embedded servers (Tomcat, Jetty, Undertow) to simplify setup.
- Jakarta EE defines the enterprise standards. Spring Boot does not replace Jakarta, but it integrates with many Jakarta APIs (e.g., JPA, Servlets) and has adapted to the new “jakarta.\*” namespaces. In practice, Spring Boot and Jakarta EE can complement each other, but Spring Boot emphasizes flexibility and developer productivity, while Jakarta EE emphasizes standardization and vendor neutrality.

Examples of Spring Boot auto-configuration defaults:

- If the spring-boot-starter-web dependency is present, it automatically configures Spring MVC and an embedded Tomcat server.
- If a database driver is detected, it configures a DataSource and JPA EntityManager.
- With Spring Security, a default login with a generated password is created unless overridden.
  Default logging setup is provided via Logback.
- Adding Spring Boot Actuator automatically exposes endpoints like /actuator/health and /actuator/metrics.

How Spring Boot relates to my studies:

Spring Boot connects directly to several areas I have learned in my Computer Science courses. From object-oriented programming to databases and SQL, it reuses and expands on these foundations. My knowledge of web programming and HTTP is applied when building REST APIs with @RestController. Concepts from software engineering and design patterns (like dependency injection and MVC) are also present in Spring’s architecture. In short, Spring Boot allows me to bring together the skills I’ve learned into real-world enterprise applications that are both practical and industry-relevant.

## 3.2 Getting started tutorial: REST web services

### Description

online spring boot tutorial

### Notes

- Annotations to ensure HTTP requests
  There are companion annotations for HTTP verbs (e.g. `@PostMapping` for POST, `@GetMapping` for GET). There is also a `@RequestMapping` annotation that they all derive from, and can serve as a synonym (e.g. `@RequestMapping(method=GET)`).

- Binding query parameter to a variable
  In the following example `@RequestParam` binds the value of the query string parameter `name` into the `name` parameter of the `greeting()` method. If the `name` parameter is absent in the request, the `defaultValue` of "World" is used.
  ```java
    @GetMapping("/greeting")
  	public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
  		return new Greeting(counter.incrementAndGet(), String.format(template, name));
  	}
  ```
- This code uses Spring `@RestController` annotation, which marks the class as a controller where every method returns a domain object instead of a view. It is shorthand for including both `@Controller` (this annotation indicates that a class is a web controller responsible for handling incoming HTTP requests) and `@ResponseBody` (this annotation is used to bind the return value of a controller method directly to the HTTP response body).

  ```java
    @RestController
    public class GreetingController {
        (...)
    }
  ```

- `@SpringBootApplication` is a convenience annotation that adds all of the following:

  - `@Configuration`: Tags the class as a source of bean definitions for the application context.
  - `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if `spring-webmvc` is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a `DispatcherServlet`.
  - `@ComponentScan`: Tells Spring to look for other components, configurations, and services in this case in the `spring_boot_tutorial` package, letting it find the controllers.
  - The `main()` method uses Spring Boot’s `SpringApplication.run()` method to launch an application. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure.

  ```java
    @SpringBootApplication
    public class RestServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(RestServiceApplication.class, args);
        }

    }
  ```

- Build an executable JAR
  - With Maven it can done with
    ```bash
    ./mvnw spring-boot:run
    ```
  - Or first build the JAR file with
    ```bash
    ./mvnw clean package
    ```
    And then run with
    ```bash
    java -jar target/gs-rest-service-0.1.0.jar
    ```

---

## 3.3 User management example

### Description

Analises of a spring boot aplication

### Notes

- Scripts analises and description:

  - #### [User.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/data/User.java)
    This class has the annotations `@Entity` and `@Table` from Jakarta Persistence that are used to designate a Java class as an entity, which represents a table in a relational database and to name that class, in this case as "users"
  - #### [UserRepository.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/data/UserRepository.java)
    This an interface has the `@Repository` annotation from Sping Framework that is a specialization of the `@Component`, it is used to indicate that a class serves as a data repository responsible for encapsulating storage, retrieval, search, update, and delete operations on objects, emulating a collection of objects
  - #### [UserService.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/services/UserService.java)
    This is an interface that defines methods to be implemented in the following class
  - #### [UserServiceImpl.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/services/UserServiceImpl.java)
    This class has the `@Service` annotation from the Spring Framework that is a specialization of the `@Component` annotation used to mark a class as a service provider within the application's service layer, it is used to indicate that a class contains business logic and provides specific functionalities related to a particular domain of the application. This class also has the `@Autowired` annotation in
    ```java
    @Autowired
    private UserRepository userRepository;
    ```
    that enables automatic dependency injection, allowing the Spring container to supply required dependencies to beans without explicit configuration
  - #### [IndexController.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/boundary/IndexController.java)
    This class has the annotations `@RestController` and `@GetMapping` from the Spring Framework that have already been described in the previous exercice, [3.2 Getting started tutorial: REST web services](#32-getting-started-tutorial-rest-web-services), this class representes the most basic "page" of the website ("/")
  - #### [GreetingController.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/boundary/GreetingController.java)
    This class has the annotations `@Controller` and `@GetMapping` from the Spring Framework that have already been described in the previous exercice, [3.2 Getting started tutorial: REST web services](#32-getting-started-tutorial-rest-web-services), this class representes the "page" of the website with routing "/greeting"
  - #### [ResourceNotFoundException.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/boundary/ResourceNotFoundException.java)
    This class has the `@ResponseStatus` annotation from the Spring Framework that is used to set custom HTTP status codes for controller methods or exception handlers, this class representes the "page" of the website with a non defined route returning an error message
  - #### [UserRestController.java](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/boundary/UserRestController.java)
    This class has the annotations `@RestController`, `@RequestMapping`, `@PostMapping`, `@GetMapping` and `@DeleteMapping` that have already been described in the previous exercice, [3.2 Getting started tutorial: REST web services](#32-getting-started-tutorial-rest-web-services), this class representes the managemente of the "pages" of the website with api users endpoints

- Packages and separation of concerns

  - #### [data package](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/data)
    This package defines the entity and its atribuitions, where that information is stored and the methods and interactions (resembling queries) that can be made with that information. Representing the data layer.
  - #### [services package](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/services)
    In this package the connection to the repository is made and new methods that use the previous queries are created to resembling the configuration of an api internal methods. Representating the service layer.
  - #### [boundary package](./lab3_3/tutorial-spring/src/main/java/com/sergiofreire/xray/tutorials/springboot/boundary)
    In this package the especific endpoints are created where some will use the methods defined in the services package. With Spring boot this endpoints configurations will be "pages" of the website.

- `spring-boot-starter-data-jpa` -> Implementation of the reference Java Persistence API (JPA).
- Specific database drivers included in this project
  - H2 Database
  ```xml
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
  ```
  - PostgreSQL JDBC Driver
  ```xml
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>
  ```

---

## 3.4 Meals booking

### Description

backend for meals booking application

### Notes

- postgresql dependency

  ```xml
  <dependency>
  		<groupId>org.postgresql</groupId>
  		<artifactId>postgresql</artifactId>
  		<scope>runtime</scope>
  	</dependency>
  ```

- start a container with PostgreSQL engine, e.g.:

  ```bash
  docker run --name postgresdb -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=meals_db -p 5432:5432 -d postgres:latest
  ```

- configure spring boot to use PostgreSQL in the [application.properties](./lab3_4/lab3meals/src/main/resources/application.properties) file with

  ```
    # PostgreSQL connection
    spring.datasource.url=jdbc:postgresql://localhost:5432/meals_db
    spring.datasource.username=admin
    spring.datasource.password=secret
    spring.datasource.driver-class-name=org.postgresql.Driver

    # Hibernate DDL auto update (creates/updates tables)
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
  ```

  `spring.jpa.hibernate.ddl-auto=update` → keeps the schema updated without dropping data

  `spring.jpa.show-sql=true` → shows SQL statements in the console (useful for debugging).

---
