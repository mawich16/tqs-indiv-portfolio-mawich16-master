# Lab Class 5 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [WebDriver “hello world”](#51-webDriver-hello-world)
2. [Interactive test recording](#52-interactive-test-recording)
3. [Selecting elements with locators](#53-selecting-elements-with-locators)
4. [Playwright: “hello world”](#55-playwright-hello-world)
5. [Playwright: Interactive test recording](#56-playwright-interactive-test-recording)
6. [Playwright: Selecting elements with locators](#57-playwright-selecting-elements-with-locators)

---

## 5.1 WebDriver “hello world”

### Notes

- Dependecies:

  ```xml
  <!-- JUnit 5 -->
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.10.1</version>
      <scope>test</scope>
  </dependency>

  <!-- Selenium WebDriver -->
  <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-java</artifactId>
      <version>4.15.0</version>
  </dependency>

  <!-- Selenium-JUnit Extension -->
  <dependency>
      <groupId>io.github.bonigarcia</groupId>
      <artifactId>selenium-jupiter</artifactId>
      <version>5.1.0</version>
      <scope>test</scope>
  </dependency>
  ```

- Had to configure Chrome options for Brave

  ```java
  @BeforeEach
  void setup() {
      // Setup ChromeDriver (Brave uses ChromeDriver)
      WebDriverManager.chromedriver().setup();

      // Configure Chrome options for Brave
      ChromeOptions options = new ChromeOptions();

      options.setBinary("/usr/bin/brave-browser");

      driver = new ChromeDriver(options);
  }
  ```

---

## 5.2 Interactive test recording

### Notes

- doesnt work correctly in brave, use firefox
- to add a title assert: `"command": "assertTitle","target": "BlazeDemo Confirmation"`
- this exercice teste is implemented in `Lab51SeleniumJupiterTests` test class

## 5.3 Selecting elements with locators

### Notes

- css identifier-based locators are more easy to find when inspecting the page
- xpath examples: `["xpath=//input[@value='']", "xpath:attributes"],["xpath=//div[@id='root']/div/div/div/div/div/div/input", "xpath:idRelative"],["xpath=//input", "xpath:position"]`

---

## 5.5 Playwright: “hello world”

### Notes

- Annotated with @UsePlaywright to enable Playwright's JUnit extension
- No manual Playwright/Browser setup or teardown
- The Page instance is injected automatically by the extension

---

## 5.6 Playwright: Interactive test recording

### Notes

- Playwright’s codegen tool automatically generates selectors using its “strict, resilient locator model”:
  | **Priority** | **Locator Type** | **Description** | **Why It’s Preferred** |
  | ------------ | --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------- |
  | 1️⃣ | **Role + accessible name (ARIA)** | e.g., `getByRole(AriaRole.BUTTON, { name: "Find Flights" })` | Based on semantic roles and labels that users (and screen readers) perceive — stable across UI changes. |
  | 2️⃣ | **Text locator** | e.g., `getByText("Find Flights")` | Simple and human-readable, but can break if the text changes. |
  | 3️⃣ | **CSS or attribute selector** | e.g., `locator("button#submit")` | More technical, depends on DOM attributes — fragile if the frontend changes. |
  | 4️⃣ | **XPath (legacy)** | e.g., `locator("//button[text()='Submit']")` | Least recommended — brittle and hard to maintain. |

- Refactor:
  | **Before** | **After** | **Why it’s better** |
  | ------------------------------------------------------------ | --------------------------------------------- | ----------------------------------- |
  | Used full text strings like `"Choose This Flight 43 Virgin"` | Selects first matching flight button | Avoids breakage due to dynamic data |
  | Many repetitive `new Page.GetByRoleOptions()` | Only used when necessary | Cleaner, easier to read |
  | Asserted visibility on changing text | Asserted structural elements (table, heading) | More stable and semantic |
  | Generic test name `test` | Clear name `testFlightBooking` | Improves readability |

---

## 5.7 Playwright: Selecting elements with locators

### Notes

- ***
