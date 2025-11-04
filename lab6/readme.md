# Lab Class 6 - Exercises

This document contains an index of exercises and dedicated spaces for the notes relative to each exercise.

---

## Index

1. [Getting started (calculator)](#61-getting-started-calculator)
2. [Passing data to tests](#62-passing-data-to-tests)
3. [Web automation (online library)](#63-web-automation-online-library)

---

## 6.1 Getting started (calculator)

### Notes

- the regular expressions style was maintined in this exercise to be a model of comparissing with the next exercises:
  ```java
  @When("^I add (\\d+) and (\\d+)$")
  ```

---

## 6.2 Passing data to tests

### Notes

- example of an used Cucumber expression:
  ```java
  @When("I search for books by date {word} to {word}")
  ```

---

## 6.3 Web automation (online library)

### Notes

- example of implementation with the interpolation of `When`, `Then` and `And` expressions in the `.feature` file

---
