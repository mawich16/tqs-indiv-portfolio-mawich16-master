# Lab Class 7 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [Static analysis with SonarQube (local instance)](#71-static-analysis-with-sonarQube-local-instance)
2. [Technical debt](#72-technical-debt)
3. [Frontend inspection (performance, accessibility and best practices)](#73-frontend-inspection-performance-accessibility-and-best-practices)
4. [7.4 AI-assisted reviews](#74-AI-assisted-reviews)

---

## 7.1 Static analysis with SonarQube (local instance)

### Notes

- Sonar POM plugin
  ```xml
  <plugin>
        <groupId>org.sonarsource.scanner.maven</groupId>
        <artifactId>sonar-maven-plugin</artifactId>
        <version>yourPluginVersion</version>
    </plugin>
  ```
- password: `UniTqsLab7..` user: `admin`
- token: `Analyze "lab7_1": sqp_a557c972fbf6cdd232803be178983707a04d709e`
- maven command to run the analysis:
  ```bash
  mvn clean verify sonar:sonar \
  -Dsonar.projectKey=lab7_1 \
  -Dsonar.projectName='lab7_1' \
  -Dsonar.host.url=http://127.0.0.1:9000 \
  -Dsonar.token=sqp_a557c972fbf6cdd232803be178983707a04d709e
  ```
- Sonar analysis:
  the project passed the quality gate, there are no open isuues regarding security and reliability but there is 1 open issue in maintainability, security review rating is A that means that at least 80% of security hotspots are reviewed, and coverage is at 84.5% wih 40 lines to cover, there is 0.0% of duplications. This are positive results allthough there ia an analysis warning: `Missing blame information for 4 files. This may lead to some features not working correctly.`
- Checkstyle, PMD, and SpotBugs are external static analysis tools that Sonar can integrate with or draw inspiration from to detect code issues

---

## 7.2 Technical debt

### Notes

- token: `Analyze "lab7_2": sqp_aa2b423cf7c96773c22940f19a929b1b2d644198`
- maven command to run the analysis (projectKey and projectName changed):
  ```bash
  mvn clean verify sonar:sonar \
  -Dsonar.projectKey=lab7_2 \
  -Dsonar.projectName='lab7_2' \
  -Dsonar.host.url=http://127.0.0.1:9000 \
  -Dsonar.token=sqp_aa2b423cf7c96773c22940f19a929b1b2d644198
  ```
- technical debt is the total estimated time required to fix all maintainability issues, also known as code smells, within a codebase. the results are presentend the format of `<file_name>`, folowed by `Lines of Code:` that is self explainatory, `Technical Debt:` that represents the estimated time required to fix all maintainability issues, `Maintainability Issues:` that is the number of maintainability issues of the specific script and `Maintainability Rating:` that is a specific Sonar scale (A is given if the remediation cost is 5% or less of the development cost, B is assigned for ratios between 6% and 10%, C corresponds to ratios between 11% and 20%, D is given for ratios between 21% and 50% and E is assigned when the remediation cost exceeds 50% of the development cost). obtanid results were:
  - MealBookingRepository.java
    Lines of Code: 12
    Technical Debt: 1min
    Maintainability Issues: 1
    Maintainability Rating: A
  - MealBookingRestController.java
    Lines of Code: 48
    Technical Debt: 5min
    Maintainability Issues: 1
    Maintainability Rating: A
  - MealBookingServiceImpl.java
    Lines of Code: 68
    Technical Debt: 5min
    Maintainability Issues: 1
    Maintainability Rating: A

---

## 7.3 Frontend inspection (performance, accessibility and best practices)

### Notes

- mobile review

  - Performance Score (86) is based primarily on the metrics:
    | Metric | Value | Meaning | Impact on Perceived Speed |
    | ---------------------------------- | ------ | --------------------------------------------------------------------------- | ------------------------------------------------------- |
    | **First Contentful Paint (FCP)** | 2.8 s | Time until the first text or image is visible. | Affects how quickly the site _appears_ to load. |
    | **Largest Contentful Paint (LCP)** | 3.4 s | Time until the largest visual element (main image/text block) is visible. | Strongly influences user perception of load completion. |
    | **Total Blocking Time (TBT)** | 90 ms | Measures main-thread blocking — time the browser couldn’t respond to input. | Impacts _responsiveness_ and smoothness. |
    | **Cumulative Layout Shift (CLS)** | 0 | Measures unexpected layout shifts. | Excellent score — users see stable layout. |
    | **Speed Index** | 2.8 s | Measures how quickly visible parts of the page are displayed. | Reflects smoothness of content appearance. |
    | **Time to Interactive (TTI)** | ~3.4 s | When the page becomes fully interactive. | Impacts when users can _use_ the site. |

  wich means:

  - LCP (3.4 s) is the main performance limiter — anything over ~2.5 s can make the site feel sluggish.
  - FCP (2.8 s) is also slightly slow; users wait nearly 3 seconds before seeing anything.
  - TBT (90 ms) and CLS (0) are excellent — your site is responsive and visually stable.

  - Improving Accessibility (Score: 95):
    | Issue | Explanation |
    | ---------------------------------- | --------------------------------------------------------------- |
    | **Insufficient color contrast** | Background and foreground colors do not meet WCAG AA standards. |
    | **Links without descriptive text** | One or more links lack descriptive text. |
    | **Manual review items (10)** | Automated tests can’t verify all accessibility aspects. |

  - to make the site more accessible, a color palette to ensure contrast a higher ratio could be used, replace empty anchors with meaningful labels for a better perception of the links should be implemented and conduct some manual audits to review the 10 items that automated tests cant check

- desktop review

  - Performance Score (100) is based primarily on the metrics:
    | Metric | Value | Meaning | Impact on Perceived Speed |
    | ---------------------------------- | ----- | ------------------------------------------------------------------------------------- | ------------------------------------------------------------ |
    | **First Contentful Paint (FCP)** | 0.6 s | Time until the first visible text or image appears. | Users see something almost instantly. |
    | **Largest Contentful Paint (LCP)** | 0.7 s | Time until the largest visual element (like a banner or hero image) is fully visible. | Excellent — users perceive the page as fully loaded quickly. |
    | **Total Blocking Time (TBT)** | 0 ms | Time the main thread was blocked and unresponsive. | Zero blocking — site is highly responsive. |
    | **Cumulative Layout Shift (CLS)** | 0 | Measures layout movement after load. | Perfect stability — no unexpected shifts. |
    | **Speed Index (SI)** | 0.6 s | How quickly visible content is displayed. | Excellent — very fast rendering. |
    | **Time to Interactive (TTI)** | 0.7 s | Time until the page is fully usable. | Page becomes interactive almost immediately. |

  wich means:

  - all Core Web Vitals (FCP, LCP, CLS) are in the “Good” range.
  - the site loads, paints, and becomes interactive within a second, offering an excellent user experience.

  - Accessibility Score is 95 (everything same as before)

- ua site review
  | Category | Score |
  | ------------------ | ------ |
  | **Performance** | **59** |
  | **Accessibility** | **88** |
  | **Best Practices** | **93** |
  | **SEO** | **92** |

  - Performance
    | Metric | Value | Meaning | Impact on Perceived Speed |
    | ---------------------------------- | ----- | ---------------------------------------------------------- | ------------------------------------------------- |
    | **First Contentful Paint (FCP)** | 1.1 s | Time until the first text or image is visible. | Good — users start seeing content quickly. |
    | **Largest Contentful Paint (LCP)** | 5.1 s | Time until the largest visible element loads. | Poor — users wait >5s for main content to appear. |
    | **Total Blocking Time (TBT)** | 60 ms | How long the main thread was blocked. | Acceptable — low blocking time. |
    | **Cumulative Layout Shift (CLS)** | 0.162 | Measures layout instability (elements moving during load). | Needs improvement — noticeable shifting. |
    | **Speed Index (SI)** | 4.0 s | How quickly content appears during load. | Moderate — users see content progressively. |
    | **Time to Interactive (TTI)** | 6.5 s | When page becomes fully interactive. | Slow — long wait before interaction possible. |

            - key problems identified
              | Category | Issue | Estimated Impact / Savings |
              | ----------------------------- | -------------------------------- | ------------------------------- |
              | **Render Blocking Resources** | CSS/JS blocking rendering | ~580 ms savings |
              | **Font Display Optimization** | Fonts not loaded efficiently | ~80 ms savings |
              | **Cache Lifetime** | Inefficient caching | ~4,830 KiB savings |
              | **Legacy JavaScript** | Old JS served to modern browsers | ~134 KiB savings |
              | **Unused JavaScript** | Large amount of unused JS | ~1,670 KiB savings |
              | **Unused CSS** | Too much unused styling | ~368 KiB savings |
              | **Defer Offscreen Images** | Images loaded before needed | ~181 KiB savings |
              | **No HTTP/2** | 10 requests using HTTP/1.x | Slower connections |
              | **Large Payload** | Total 10 MB network size | High download time |
              | **Main Thread Work** | 3.2 s processing | CPU-heavy rendering |
              | **Layout Shifts** | CLS = 0.162 | Unstable visuals during load |

    - Accessibility
      | Issue | Explanation | Fix |
      | --------------------------- | ----------------------------------------------------------------------------------------------- | ------------------------------------------------------------- |
      | **ARIA misusage** | Elements with `[aria-hidden="true"]` still contain focusable elements (e.g., links or buttons). | Remove focusable elements inside hidden containers. |
      | **Low color contrast** | Text/background combinations fail WCAG contrast ratio. | Adjust color palette to meet 4.5:1 contrast for text. |
      | **Touch targets too small** | Buttons/links are too close or too small. | Ensure interactive elements are ≥48×48px and properly spaced. |
      | **Manual checks (10)** | Screen reader, keyboard navigation, etc. | Conduct manual testing. |

---

## 7.4 AI-assisted reviews

### Notes

- prompts and AI (in this case specific Claude.ai in this [chat](https://claude.ai/share/6c22d326-af1f-4192-9545-d3f132ba484e)) responses:

  - about /service directory => what can i do to make this code cleaner?

    1. **Add Missing `@Service` Annotation**

    `CityServiceImpl` is missing the `@Service` annotation, which will prevent Spring from detecting it as a bean.

    2. **Improve Exception Handling**

    Replace generic `RuntimeException` with custom, meaningful exceptions:

    - Create custom exceptions like `TripNotFoundException`, `SeatNotAvailableException`, etc.
    - This makes error handling more precise and easier to test

    3. **Extract Magic Numbers and Strings**

    Consider using constants or configuration for repeated values.

    4. **Reduce Method Complexity**

    `ReservationServiceImpl.createReservation()` and `changeReservation()` are doing too much. Extract validation and seat management logic into separate private methods.

    5. **Remove Code Duplication**

    The seat validation and reservation logic in `createReservation()` and `changeReservation()` is duplicated. Extract it into reusable methods.

    6. **Fix Transaction Management**

    Add `@Transactional` annotations to methods that modify multiple entities to ensure data consistency.

    7. **Improve Logging**

    - Use parameterized logging consistently (you're already doing this well)
    - Remove redundant debug logs that don't add value
    - Consider logging at appropriate levels (ERROR for exceptions, WARN for business rule violations)

    8. **Remove Unused Code**

    The `getTrips()` method in `TripServiceImpl` throws `UnsupportedOperationException` - either implement it or remove it from the interface.

    9. **Use Optional Properly**

    Instead of returning `null` from `getTripById()` and `getCityById()` in `TripServiceImpl`, return `Optional<Trip>` and `Optional<City>`.

    10. **Inconsistent Error Handling**

    Some methods throw `RuntimeException`, others throw `ResponseStatusException`. Be consistent - preferably use custom exceptions in the service layer and handle them in a controller advice.

  - about /repository directory => what can i do to make this code cleaner?

    1. **Fix Method Overloading Issue in PaymentRepository**
       You have two `findByTransactionId` methods with different parameter types (String and UUID). This is confusing:

    ```java
    public List<Payment> findByTransactionId(String transactionId);  // Returns List
    Payment findByTransactionId(UUID transactionId);  // Returns single Payment
    ```

    **Solution:** Remove the String version if you're only using UUID, or rename one of them to clarify intent.

    2. **Use Optional for Single Entity Queries**
       Methods that return a single entity should return `Optional<T>` to explicitly handle the "not found" case:

    ```java
    // Instead of:
    Reservation findByToken(UUID token);

    // Use:
    Optional<Reservation> findByToken(UUID token);
    ```

    Apply this to `PaymentRepository.findByTransactionId(UUID)` as well.

    3. **Remove Unnecessary `public` Modifiers**
       Interface methods are public by default, so the `public` keyword is redundant:

    ```java
    // Instead of:
    public List<City> findByNameContainingIgnoreCase(String name);

    // Use:
    List<City> findByNameContainingIgnoreCase(String name);
    ```

    4. **Remove Unnecessary Comments**
       The comment in `CityRepository` states the obvious about Spring Data JPA. Remove it unless it provides unique insight.

    5. **Consider Query Method Naming**
       Some query methods could be more specific:

    - `TripRepository.findByRouteOriginName` → Consider `findByRouteOriginNameOrderByDepartureTimeAsc` for predictable ordering
    - Add pagination support for methods that could return large datasets

    6. **Add Custom Queries for Complex Operations**
       For better performance and clarity, consider adding `@Query` annotations for complex lookups:

    ```java
    @Query("SELECT s FROM Seat s WHERE s.trip.id = :tripId AND s.reserved = false AND s.type = :type")
    List<Seat> findAvailableSeatsByTripIdAndType(@Param("tripId") Long tripId, @Param("type") SeatType type);
    ```

    7. **Add Method Documentation**
       For non-obvious query methods, add JavaDoc to explain the business logic or specific behavior.

    Additional Recommendations:

    **Consider adding these useful queries:**

    - `SeatRepository.findByTripIdAndReservedFalse(Long tripId)` - Get all available seats for a trip
    - `TripRepository.findByRouteOriginNameAndDepartureTimeAfter(String originName, LocalDateTime dateTime)` - Get future trips only
    - Add pagination support: `Page<Trip> findByRouteOriginName(String originName, Pageable pageable)`

  - about /model directory => what can i do to make this code cleaner?

        1.  **Remove Useless Constructor in Reservation**
            There's a constructor that does nothing and will cause bugs:

        ```java
        public Reservation(String passengerName2, String documentNumber2, String email2, String phone2,
                        LocalDateTime now, UUID token2, List<Seat> seats2, Trip trip2) {
            // Empty body - this is useless and dangerous!
        }
        ```

        2.  **Fix Data Inconsistency in Trip**
            You're tracking `availableSeats` separately from the actual `seats` list. The `hasAvailableSeats()` method counts reserved seats from the list, but `reserveSeats()` modifies the counter. This will cause data corruption!

        3.  **Missing JPA Annotations**

        - `Seat` and `Route` are missing no-arg constructors (required by JPA)
        - Several entities have inconsistent mapping strategies

        4.  **Incorrect Column Mapping**
            In `Trip.java`, `currentLocation` is a `City` object but has `@Column(length = 100)` - it should be `@ManyToOne` with `@JoinColumn`

        5.  **Missing Validation Annotations**
            Add `@NotNull`, `@NotBlank`, `@Email`, `@Positive` where appropriate

        ## Design Issues:

        6.  **Bidirectional Relationship Issues**

        - `Seat` has `@ManyToMany` with `Reservation` but also `@ManyToOne` with `Trip` - this seems confused
        - Missing helper methods to maintain bidirectional consistency

        7.  **Business Logic in Entities**
            Methods like `reserveSeats()` and `releaseSeats()` should be in a service, not the entity. Entities should be anemic data holders.

        8.  **Unused/Redundant Fields**

        - `Trip.normalSeats` and `Trip.premiumSeats` - not being used consistently
        - `Trip.totalSeats` - can be calculated from seats.size()

        9. **Missing equals() and hashCode()**
        For entities used in collections (especially with bidirectional relationships), you need proper `equals()` and `hashCode()` implementations.

        10. **Enum Formatting**
            `ReservationStatus` and `PaymentStatus` have inconsistent spacing: `CONFIRMED,CANCELLED, PENDING`

  - about /dto directory => what can i do to make this code cleaner?

    1.  **Add Validation Annotations**
        DTOs should validate incoming data before it reaches the service layer.

    2.  **Use Records (Java 14+)**
        If you're on Java 14+, records are perfect for immutable DTOs - much cleaner and less boilerplate.

    3.  **Add JavaDoc**
        Document what each DTO is used for and any constraints.

    4.  **Use Proper Types**
        `CreatePaymentRequest.paymentType` is a String but should be the `PaymentType` enum.

    5.  **Remove Unused Imports**
        `CreatePaymentRequest` imports `UUID` but doesn't use it.

    6.  **Consider Builder Pattern**
        For DTOs with many fields, builders improve readability.

    7.  **Add Constructors**
        No-arg constructors for frameworks, all-args for testing.

    8.  **Make DTOs Immutable**
        Use final fields and only getters for better thread safety and immutability.

  - about /controller directory => what can i do to make this code cleaner?

    1.  **Missing `@Valid` Annotation**

    Your DTOs won't be validated without `@Valid` on `@RequestBody`

    2.  **Poor Exception Handling**

    Catching generic `Exception` and `RuntimeException` hides the real errors and makes debugging difficult

    3.  **Business Logic in Controller**

    `PaymentController.createPayment()` has entity construction logic that should be in the service layer

    4.  **Inconsistent Error Responses**

    Different endpoints return errors differently (some throw exceptions, some return null checks)

    5.  **Redundant Try-Catch Blocks**

    Almost identical try-catch in every method adds noise

    6.  **No DTOs for Responses**

    Returning entities directly exposes internal structure and can cause JSON serialization issues (circular references)

    7.  **Unused Import**

    `TripController` imports `IOException` but never uses it

    8.  **Manual String-to-Enum Conversion**

    `PaymentController` manually converts String to enum - this should be automatic

  - about /config directory => what can i do to make this code cleaner?

    1. **Inconsistent with Refactored Entities**
       Your `Trip` constructor and seat management don't match the cleaner entity model we created earlier.

    2. **Hardcoded Test Data**
       All data is hardcoded in one large method, making it difficult to:

    - Understand what's being created
    - Modify or extend
    - Reuse in tests

    3. **No Separation of Concerns**
       Everything is in one method - cities, routes, trips, reservations, payments all mixed together.

    4. **Manual Relationship Management**
       You're manually setting seats on trips, which should be handled by helper methods or proper entity relationships.

    5. **Redundant Counter Fields**
       Still setting `totalSeats`, `availableSeats`, etc., which we removed in the refactored entities.

    6. **Magic Numbers**
       Hardcoded prices, seat counts, and other values without constants.

    7. **Poor Readability**
       Hard to see the structure and relationships at a glance.

    8. **Not Profile-Aware**
       Runs in all environments - should only load sample data in dev/test profiles.

- after asking the AI to add "bad smells" to the [script](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabase.java) inside the /config directory the result was a new code in [this file](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java) with the following new "bad smells":
  1. [Public fields](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L17)
  2. [Assigning to fields instead of using parameters](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L30)
  3. [Try-catch that catches everything and does nothing](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L36)
  4. [Dead code / Commented out code](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L38)
  5. [Unnecessary complexity](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L46)
  6. [Inconsistent formatting](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L55)
  7. [Variable reuse / confusing names](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L60)
  8. [Copy-paste programming with slight variations](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L66)
  9. [Empty catch block (swallowing exceptions)](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L81)
  10. [Code continues even if exception occurred](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L86)
  11. [Using System.out instead of logger](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L87)
  12. [Unused method](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L91)
  13. [Method that does nothing useful](./lab7_4/ua_tqs_gs20-master-gs-buser/gs-buser/src/main/java/com/buser/config/LoadDatabaseWorstVersion.java?#L96)

---
