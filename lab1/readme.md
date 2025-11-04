# Lab Class 1 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [Using tests in Maven project](#11-using-tests-in-maven-project)
2. [Simple Stack unit](#12-simple-stack-unit)
3. [Meals booking service](#13-meals-booking-service)

---

## 1.1 Using tests in Maven project

### Description

This exercise aims to pratice and understand maven commands and java tests

### Notes

- mvn commads tested:

```bash
mvn test
mvn clean
mvn dependecy:tree
mvn clean test package
```

- New tests:

```java
assertEquals(1.1818, t.divide(13, 11));
```

Fail response:

```
AssertionFailedError: expected: <1.1818> but was: <1.1818181818181819>
```

When a test failes the follow arent tested, it stops

New code and tests:

- Exponencial
  tests were made to the edge cases, number powerd by 0 that always must equal 1, number powerd by a negative number, and the a number powerd to a positive number, for both positive and negative cases of the first number

- Square root
  not supporting imaginary number resultaka the result for negative numbers.
  tests were made to numbers positive, negative and 0

---

## 1.2 Simple Stack unit

### Description

This exercise aims to develop the tests and after that a specific code based on them

### Notes

- First a skeleton was made and the methods didnt do actually nothing, only returned moked values to be possible to compile like null, false and always 0 to the size method.
- Then the test were created to evaluate the following situations:
  - A stack is empty on construction
  - A stack has size 0 on construction
  - After n > 0 pushes to an empty stack, the stack is not empty and its size is n
  - If one pushes x then pops, the value popped is x
  - If one pushes x then peeks, the value returned is x, but the size stays the same
  - If the size is n, then after n pops, the stack is empty and has a size 0
  - Popping from an empty stack throws a NoSuchElementException
  - Peeking into an empty stack throws a NoSuchElementException
- In the file `TqsStackTestAI.java` are the AI generated unit tests for comparising. All tests passed without any errors
- The `TqsStack` code will fail if we try to se the method `popTopN(int n)` and n is supperior to the stack size, to cover this case an addictional test should be added and the method should verify if the input value is valid

---

## 1.3 Meals booking service

### Description

Implement a meals booking service and respective tests and start using jacoco maven's plugin

### Notes

- Relevant situations and edge cases to implement:
  - No double booking
  - Capacity limit
  - No reuse of used/canceled tickets

The command:

```bash
mvn clean test jacoco:report
```

compiles and tests the code and generates a report `target/site/jacoco/index.html` that can be opened in the browser with:

```bash
brave-browser target/site/jacoco/index.html
```

Guide lines to interper the report:

- Coverage metrics shown:
  - Instructions → percentage of bytecode instructions executed during tests.
  - Branches → conditional branches (if/else, switch) covered.
  - Lines → which source lines were executed.
  - Methods / Classes → what % of methods and classes were touched.
- Color coding:
  - Green → covered by tests.
  - Red → not covered at all.
  - Yellow → partially covered (e.g., one branch of an if executed).
- Navigation:
  - The top-level table shows the packages.
  - Click into a package → see per-class coverage.
  - Click a class → see source code with highlighted coverage.

Report results:

- MealBookingRequest => all 100%
- ReservationState => all 100%
- MealsBookingService => instructions 93%, branches 72%, report shows that exceptions weren't tested (correspondent lines in red)

Jacoco plugin in `pom.xml`

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.11</version>
  <executions>
      <execution>
          <goals>
              <goal>prepare-agent</goal>
          </goals>
      </execution>
      <execution>
          <id>report</id>
          <phase>test</phase>
          <goals>
              <goal>report</goal>
          </goals>
      </execution>
  </executions>
</plugin>
```

Jacoco rule to ensure a minimum of 90% line coverage

```xml
<execution>
  <id>check</id>
  <phase>verify</phase>
  <goals>
      <goal>check</goal>
  </goals>
  <configuration>
      <rules>
          <rule>
              <element>BUNDLE</element>
              <limits>
                  <limit>
                      <counter>INSTRUCTION</counter>
                      <value>COVEREDRATIO</value>
                      <minimum>0.90</minimum>
                  </limit>
              </limits>
          </rule>
      </rules>
  </configuration>
</execution>
```

Here we have set the coverage threshold at the BUNDLE level which represents the overall coverage of the entire codebase being analyzed. It combines the coverage information from different units, such as classes, packages, or modules, into a single aggregated result.

The INSTRUCTION counter is valuable for understanding the level of code execution and identifying areas that may have been missed by the tests. The BRANCH counter enables the identification of decision point coverage, ensuring that it exercises both true and false branches.

Command to check the new execution, if it fails is because any non-excluded class has <90% line coverage (in this case)
(couldn't run with `mvn clean test jacoco:check`)

```bash
mvn clean verify
```

pom.xml code to exclude `MealBookingRequest` and `ReservationState` classes

```xml
<configuration>
  <excludes>
      <exclude>**/MealBookingRequest*</exclude>
      <exclude>**/ReservationState*</exclude>
  </excludes>
</configuration>
```

---
