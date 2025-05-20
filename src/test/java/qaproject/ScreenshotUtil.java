package qaproject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class for handling screenshot operations in test automation
 */
public class ScreenshotUtil {
    
    private static final String SCREENSHOT_DIR = "test-output/screenshots";
    
    /**
     * Takes a screenshot of the current browser window
     * 
     * @param driver WebDriver instance
     * @param testName Name of the test for screenshot naming
     * @param reason Optional reason for taking screenshot (e.g., "failure", "verification")
     * @return Path to the saved screenshot file
     */
    public static String takeScreenshot(WebDriver driver, String testName, String reason) {
        if (driver == null) {
            System.err.println("Cannot take screenshot: WebDriver is null");
            return null;
        }
        
        // Create screenshots directory if it doesn't exist
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        
        // Generate unique filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + (reason != null ? reason + "_" : "") + timestamp + ".png";
        String screenshotPath = SCREENSHOT_DIR + File.separator + screenshotName;
        
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Takes a screenshot of a specific WebElement
     * 
     * @param driver WebDriver instance
     * @param element WebElement to capture
     * @param testName Name of the test for screenshot naming
     * @param elementName Name of the element being captured
     * @return Path to the saved screenshot file
     */
    public static String takeElementScreenshot(WebDriver driver, WebElement element, String testName, String elementName) {
        if (driver == null || element == null) {
            System.err.println("Cannot take element screenshot: WebDriver or WebElement is null");
            return null;
        }
        
        // Create screenshots directory if it doesn't exist
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        
        // Generate unique filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + elementName + "_" + timestamp + ".png";
        String screenshotPath = SCREENSHOT_DIR + File.separator + screenshotName;
        
        try {
            File source = element.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            System.out.println("Element screenshot saved: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to capture element screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Takes a screenshot at a specific step in the test
     * 
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @param stepName Name of the test step
     * @return Path to the saved screenshot file
     */
    public static String takeStepScreenshot(WebDriver driver, String testName, String stepName) {
        return takeScreenshot(driver, testName, "step_" + stepName);
    }
    
    /**
     * Cleans up old screenshots based on retention policy
     * 
     * @param daysToKeep Number of days to keep screenshots
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists() || !screenshotDir.isDirectory()) {
            return;
        }
        
        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L);
        File[] files = screenshotDir.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        System.out.println("Deleted old screenshot: " + file.getName());
                    }
                }
            }
        }
    }
}