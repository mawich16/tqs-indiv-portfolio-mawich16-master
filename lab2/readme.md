# Lab Class 2 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [Stocks portfolio](#21-stocks-portfolio)
2. [Conversion method behavior](#22-conversion-method-behavior)
3. [Connect to remote resource and integration tests](#23-connect-to-remote-resource-and-integration-tests)

---

## 2.1 Stocks portfolio

### Description

This exercise aims to pratice the usage of mocks and different assertions

### Notes

- The following code was used to create the mocks

```java
@InjectMocks
private StocksPortfolio stocksPortfolio;

@Mock
private IStockmarketService service;
```

- Mockito can "simulate" a method beaviour with code like:

```java
when(service.lookUpPrice("AMZ")).thenReturn(10.0);
```

That results in the return of "10.0" when the method `lookUpPrice(String s)` is called on a stock with label "AMZ"

- Mockito also alows the verification of the number of times that a method is called on a specific object

```java
verify(service, times(1)).lookUpPrice("AMZ");
```

This verifies that the method `lookUpPrice(String s)` for a stock with label "AMZ" to th object s was made only 1 time during the test

- When trying to get extra "stock" types the following error appeared: `Unnecessary stubbings detected. Clean & maintainable test code requires zero unnecessary code.`

```java
when(service.lookUpPrice("AAPL")).thenReturn(150.0);
when(service.lookUpPrice("GOOG")).thenReturn(2800.0);
when(service.lookUpPrice("TSLA")).thenReturn(700.0);
```

- The following hamcrest is an example of the assertions that were used:

```java
assertThat(s, hasProperty("label", equalTo("AMZ")));
```

- pom.xml hamcrest dependency configuration

```xml
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest</artifactId>
    <version>2.2</version>
    <scope>test</scope>
</dependency>
```

This assertion is checking if the object s has the property "label" with the value "AMZ"

- For the new method `mostValuableStocks(int TopN)` the following cases where tested:
  - top 3 with 5 existing stocks (normal, it should return 3 elements order)
  - top 5 with 2 existing stocks (higher len of top list requested for the number of existing stocks, it should return the existing stocks in order)
  - top 3 with 0 existing stocks (it should return an empty list)

---

## 2.2 Conversion method behavior

### Description

This exercise has the goal to mock an external API

### Notes

- To simulate the usage of an API, its reponse was mocked. With the help of the API's documentation to ensure the correct format and with the usage of Mockito properties like `when(...).thenReturn()` it was possible to simulate an API request

```java
String fakeJson = """
{
"id": 3,
"title": "Mens Cotton Jacket",
"price": 55.99,
"description": "great jacket",
"category": "men's clothing",
"image": "http://example.com"
}
""";

when(httpClient.doHttpGet("https://fakestoreapi.com/products/3"))
    .thenReturn(fakeJson);
```

---

## 2.3 Connect to remote resource and integration tests

### Description

This exercise aims to test a real API instead of mocking its beaviour

### Notes

- pom.xml "failsafe" maven plugin configuration

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.5.4</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

- Questions:

  1. Was it easy to swap between the mock and the real implementation, in tests? How did it affect the design of the production code?

     - Yes, it was very easy to swap implementations. The main aspects to considere were:
       1. Dependency injection via constructor: The `ProductFindService` receives `ISimpleHttpClient` as a constructor parameter, not instantiating it internally
       2. Interface-based Design: Using the ISimpleHttpClient interface allows any implementation (mock or real) to be injected
       3. No impact on production code: The production code (`ProductFindService`) didn't need any changes when switching from mock to real implementation
       4. However, an issue emerged during integration testing: The actual API response contained a `rating` field that was not present in the `Product` class definition. This caused the JSON deserialization to fail when using the real HTTP client. To resolve this discrepancy, the `@JsonIgnoreProperties(ignoreUnknown = true)` annotation was added to the `Product` class, configuring Jackson to ignore unmapped fields in the JSON response.

  2. What would be the consequence of instantiating the dependency (the HTTP client) inside the service constructor (instead of being passed as a parameter)?
     - Severe consequences:
       1. Impossible to test with mocks: You couldn't inject a mock HTTP client, forcing all tests to make real HTTP calls
       2. No flexibility: Cannot swap implementations (e.g., for different environments, retry logic, or caching)
       3. Integration tests become unit tests: You lose the ability to have fast, isolated unit tests

- Commands comparison

  - ```bash
        mvn test
    ```

    - Runs only unit tests (classes ending in Test)
    - Fast execution, uses mocks
    - Skips integration tests

  - ```bash
        mvn package
    ```

    - Compiles code, runs unit tests, and packages into JAR/WAR
    - Doesn't run integration tests by default
    - Fails if unit tests fail

  - ```bash
        mvn package -DskipTests=true
    ```

    - Packages the application without running any tests
    - Faster build, but risky (untested code)
    - Used for quick builds or when tests are already verified

  - ```bash
        mvn failsafe:integration-test
    ```

    - Runs only integration tests (classes ending in `IT`)
    - Uses real implementations
    - Requires the `maven-failsafe-plugin` configuration

  - ```bash
        mvn install
    ```

    - Does everything package does, plus installs artifact to local Maven repository (~/.m2)
    - Makes the artifact available to other local projects
    - Runs unit tests but not integration tests (unless configured)

  - ```bash
        mvn verify
    ```
    - Runs both unit tests and integration tests in the correct lifecycle phases.

---
