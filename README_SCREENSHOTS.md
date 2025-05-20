# Screenshot Functionality in Test Automation

This document describes the screenshot functionality implemented in the Mastodon test suite.

## Overview

The screenshot functionality provides automated capture of UI images during test execution for validation and debugging purposes. Screenshots are captured in the following scenarios:

- Automatically on test failures
- At key test steps
- For UI verification through comparison
- For specific UI elements

## Components

### 1. ScreenshotUtil

A utility class that handles the core screenshot functionality:

- `takeScreenshot(WebDriver, String, String)`: Captures full page screenshots
- `takeElementScreenshot(WebDriver, WebElement, String, String)`: Captures screenshots of specific elements
- `takeStepScreenshot(WebDriver, String, String)`: Captures screenshots at specific test steps
- `cleanupOldScreenshots(int)`: Manages screenshot retention based on age

### 2. UIVerificationUtil

A utility class for visual testing through screenshot comparison:

- `verifyScreenshotWithBaseline(WebDriver, String, String)`: Compares current UI with baseline
- `verifyElementWithBaseline(WebDriver, WebElement, String, String, String)`: Compares element with baseline
- `createBaseline(WebDriver, String)`: Creates baseline screenshots for future comparisons
- `createElementBaseline(WebDriver, WebElement, String, String)`: Creates element baselines

### 3. Enhanced CustomTestListener

The TestNG listener has been enhanced to automatically capture screenshots:

- On test failures
- Optionally on test success (commented out by default)
- On tests that fail but are within success percentage
- Cleanup of old screenshots when test suite finishes

## Usage Examples

The `ScreenshotDemoTest` class demonstrates how to use the screenshot functionality:

### Taking Screenshots at Key Steps

```java
// Take screenshot at a specific step
ScreenshotUtil.takeStepScreenshot(driver, "testName", "stepName");

// Take screenshot of a specific element
WebElement element = driver.findElement(By.id("elementId"));
ScreenshotUtil.takeElementScreenshot(driver, element, "testName", "elementName");
```

### UI Verification

```java
// Create a baseline (first run only)
String baselinePath = UIVerificationUtil.createBaseline(driver, "homepageBaseline");

// Compare current UI with baseline
boolean matches = UIVerificationUtil.verifyScreenshotWithBaseline(
    driver, "testName", baselinePath);
Assert.assertTrue(matches, "UI verification failed");
```

## Screenshot Storage

Screenshots are stored in the following directories:

- Regular screenshots: `test-output/screenshots/`
- Comparison images: `test-output/screenshot-comparisons/`

Screenshot files are named using the pattern: `testName_reason_timestamp.png`

## Best Practices

1. **Selective Capture**: Only capture screenshots at key points to avoid performance impact
2. **Meaningful Names**: Use descriptive test and step names for easy identification
3. **Retention Policy**: Configure appropriate retention period (default: 30 days)
4. **Headless Mode**: Consider using headless browser mode for consistent screenshots in CI/CD
5. **Element Screenshots**: Use element-specific screenshots when possible to reduce image size

## Limitations

- Screenshot comparison may be affected by dynamic content or animations
- Different screen resolutions may affect comparison results
- Storage requirements increase with test frequency and screenshot count

## Future Enhancements

- Integration with cloud storage for long-term retention
- Advanced image comparison algorithms
- Video recording for complex test scenarios
- Annotation of screenshots with test metadata
