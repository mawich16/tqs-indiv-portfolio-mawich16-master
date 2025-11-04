# Lab Class 4 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [Employee management (getting started example)](#41-employee-management-getting-started-example)
2. [Meals booking API (test slicing)](#42-meals-booking-api-test-slicing)
3. [Testing practices](#43-testing-practices)
4. [Rest Assured library](#44-rest-assured-library)

---

## 4.1 Employee management (getting started example)

### Description

Study the provided Spring Boot example

### Notes

- This command executes the tests were the class name has the defined prefix

  ```bash
  mvn test -Dtest=EmployeeService*
  ```

- What is the difference between standard `@Mock` and `@MockBean`?

  - `@Mock` => is a standard annotation from the Mockito framework used for creating mock objects in unit tests where the Spring context is not required or loaded; it is ideal for testing classes in isolation
  - `@MockBean` => is a Spring Boot-specific annotation designed for integration tests where the Spring application context is loaded; it adds or replaces a bean within the Spring context with a mock, allowing for seamless integration testing

- What is the role of the file “application-integrationtest.properties”? In which conditions will it be used?

  By default the file `application.properties` is used to define the proprerties used in production code, but when the `@TestPropertySource(locations = "application-integrationtest.properties")` annotation is present, it overrides th default properties so, `application-integrationtest.properties` is a test-specific configuration file used when the integration tests need a different setup (like a separate database). It is only applied to tests that explicitly reference it with:

  ```java
  @TestPropertySource(locations = "application-integrationtest.properties")
  ```

---

## 4.2 Meals booking API (test slicing)

### Description

Add tests to meals booking application

### Notes

- a) Tests were made to simulate the endpoints created in `MealBookingRestController`
- b) Important edge cases like invalid IDs, null values, and attempting operations on bookings in inappropriate states and some tests to verify exception messages contain specific text
- c) Database dependency in the POM (e.g.: H2)
  ```xml
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
  ```

---

## 4.3 Testing practices

### Notes

- a) Yes, the top-down approach aligns with TDD (Test-Driven Development) principles. It starts with the service layer (highest level) by writing tests first, mocks the dependencies (repositories, external services) without implementing them yet, defines the expected behavior through test assertions and implements just enough code to make the test pass.
- b) Core Tests to Implement:
  - Service Layer:
    - Find exact match replacement:
      ```java
      @Test
      void whenFindReplacement_withExactMatch_thenReturnSimilarCar() {
      // Given: Original car (Sedan, Petrol, Medium segment)
      // And: Available car with same attributes
      // When: findReplacement(originalCar)
      // Then: Return the matching available car
      }
      ```
      - No available replacements:
      ```java
      @Test
      void whenFindReplacement_withNoAvailable_thenReturnEmpty() {
      // Given: Original car specifications
      // And: No cars matching criteria are available
      // When: findReplacement(originalCar)
      // Then: Return Optional.empty()
      }
      ```
      - Exclude currently rented cars:
      ```java
      @Test
      void whenFindReplacement_excludeRentedCars_thenReturnOnlyAvailable() {
      // Given: Similar cars exist but some are rented
      // When: findReplacement(originalCar)
      // Then: Return only available (non-rented) car
      }
      ```
    - Repository Layer:
      - Find by segment and motor type
        ```java
        @Test
        void whenFindBySegmentAndMotorType_thenReturnMatchingCars() {
            // Query database for specific segment and motor type
            // Verify correct cars are returned
        }
        ```
      - Find available cars only
        ```java
        @Test
        void whenFindAvailableCars_thenExcludeRented() {
            // Query for cars not in active rentals
            // Verify no rented cars in results
        }
        ```
    - Integration Tests:
      - End-to-end replacement scenario:
        ```java
        @SpringBootTest
        @AutoConfigureTestDatabase
        @Test
        void whenClientNeedsCourtesyCar_thenFindAndAssignReplacement() {
            // Create test data in database
            // Call service through REST API or direct service call
            // Verify correct replacement car is found
            // Verify car is marked as assigned
        }
        ```

---

## 4.4 Rest Assured library

### Description

Rest Assured library usage

### Notes

- pom.xml dependency
  ```xml
  <dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
  </dependency>
  ```

---
