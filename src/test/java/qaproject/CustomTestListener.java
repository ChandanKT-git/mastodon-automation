package qaproject;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Custom TestNG listener that provides enhanced test reporting and screenshot capture
 */

public class CustomTestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== Starting Test Suite: " + context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n========== Finished Test Suite: " + context.getName() + " ==========");
        System.out.println("Total tests run: " + context.getAllTestMethods().length);
        System.out.println("Passed tests: " + context.getPassedTests().size());
        System.out.println("Failed tests: " + context.getFailedTests().size());
        System.out.println("Skipped tests: " + context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\nStarting test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
        
        // Optionally capture screenshot on success (can be enabled/disabled based on configuration)
        // captureScreenshot(result, "success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getMethod().getMethodName());
        System.out.println("Failure details: " + result.getThrowable().getMessage());
        
        // Capture screenshot on failure
        captureScreenshot(result, "failure");
    }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            System.out.println("Skip reason: " + result.getThrowable().getMessage());
        }
        
        // Optionally capture screenshot on skipped tests
        // captureScreenshot(result, "skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Failed Within Success Percentage: " + result.getMethod().getMethodName());
        
        // Capture screenshot for tests that failed but within success percentage
        captureScreenshot(result, "failedWithinSuccessPercentage");
    }
    
    /**
     * Helper method to capture screenshots
     * 
     * @param result The test result
     * @param reason Reason for taking the screenshot
     */
    private void captureScreenshot(ITestResult result, String reason) {
        Object currentClass = result.getInstance();
        WebDriver driver = null;
        
        try {
            driver = (WebDriver) currentClass.getClass().getDeclaredField("driver").get(currentClass);
        } catch (Exception e) {
            System.err.println("Could not access WebDriver instance: " + e.getMessage());
            return;
        }
        
        if (driver != null) {
            String testName = result.getMethod().getMethodName();
            ScreenshotUtil.takeScreenshot(driver, testName, reason);
        }
    }
    
    /**
     * Cleanup old screenshots when test suite finishes
     * Default retention policy: keep screenshots for 30 days
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n========== Finished Test Suite: " + context.getName() + " ==========\n");
        System.out.println("Total tests run: " + context.getAllTestMethods().length);
        System.out.println("Passed tests: " + context.getPassedTests().size());
        System.out.println("Failed tests: " + context.getFailedTests().size());
        System.out.println("Skipped tests: " + context.getSkippedTests().size());
        
        // Clean up old screenshots (keep for 30 days by default)
        ScreenshotUtil.cleanupOldScreenshots(30);
    }
}