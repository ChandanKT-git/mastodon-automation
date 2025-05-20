# Running TestNG Tests with testng.xml

## Overview

This document provides instructions on how to run the Mastodon test suite using the TestNG XML configuration file. The `testng.xml` file organizes tests into logical groups and controls test execution.

## TestNG XML Structure

The `testng.xml` file in this project organizes tests into the following categories:

1. Window Handling Tests
2. Form Interaction Tests
3. Wait Strategy Tests
4. XPath and CSS Selector Tests
5. UI Interaction Tests
6. JavaScript and Alert Tests
7. Exception Handling Tests

## Running Tests in Eclipse

### Method 1: Using TestNG Eclipse Plugin

1. Right-click on the `testng.xml` file in the Project Explorer
2. Select "Run As" > "TestNG Suite"

### Method 2: Running Individual Test Classes

1. Right-click on any test class (e.g., `MastodonWindowHandlingTest.java`)
2. Select "Run As" > "TestNG Test"

### Method 3: Running from TestNG View

1. Open the TestNG view in Eclipse (Window > Show View > Other > TestNG > TestNG)
2. Click on the "Run" button and select the `testng.xml` file

## TestNG Annotations

TestNG uses annotations to control test execution flow:

- `@BeforeSuite`: Runs once before the entire test suite
- `@BeforeTest`: Runs once before all tests in a <test> tag
- `@BeforeClass`: Runs once before any test methods in a class
- `@BeforeMethod`: Runs before each @Test method
- `@Test`: Marks a method as a test case
- `@AfterMethod`: Runs after each @Test method
- `@AfterClass`: Runs once after all test methods in a class
- `@AfterTest`: Runs once after all tests in a <test> tag
- `@AfterSuite`: Runs once after the entire test suite

## Execution Order

@BeforeSuite → @BeforeTest → @BeforeClass → (@BeforeMethod → @Test → @AfterMethod) per test → @AfterClass → @AfterTest → @AfterSuite

## Common Test Attributes

| Attribute          | Purpose                     | Example                                                 |
| ------------------ | --------------------------- | ------------------------------------------------------- |
| priority           | Defines execution order     | `@Test(priority = 1)`                                   |
| enabled            | Enables/disables a test     | `@Test(enabled = false)`                                |
| expectedExceptions | Passes if exception thrown  | `@Test(expectedExceptions = ArithmeticException.class)` |
| timeOut            | Fails if test exceeds time  | `@Test(timeOut = 2000)`                                 |
| invocationCount    | Run test multiple times     | `@Test(invocationCount = 3)`                            |
| dependsOnMethods   | Run after specified methods | `@Test(dependsOnMethods = "loginTest")`                 |
| groups             | Assign test to groups       | `@Test(groups = {"smoke", "regression"})`               |
| dataProvider       | Supply data to test         | `@Test(dataProvider = "userData")`                      |
| description        | Add test description        | `@Test(description = "Login test")`                     |
