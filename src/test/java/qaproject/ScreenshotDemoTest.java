package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Demo test class showing how to use screenshot functionality in test automation
 */
public class ScreenshotDemoTest {
    
    private WebDriver driver;
    private static final String BASE_URL = "https://mastodon.social/";
    
    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for CI/CD environments
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Demonstrates taking screenshots at key steps in a test
     */
    @Test(groups = {"screenshotDemo"})
    public void demonstrateStepScreenshots() {
        // Navigate to the homepage
        driver.get(BASE_URL);
        
        // Take screenshot at the homepage
        ScreenshotUtil.takeStepScreenshot(driver, "demonstrateStepScreenshots", "homepage");
        
        // Find and click on the login button
        WebElement loginButton = driver.findElement(By.cssSelector("a[href='/auth/sign_in']"));
        
        // Take screenshot of the login button element
        ScreenshotUtil.takeElementScreenshot(driver, loginButton, "demonstrateStepScreenshots", "loginButton");
        
        // Click the login button
        loginButton.click();
        
        // Take screenshot after clicking login
        ScreenshotUtil.takeStepScreenshot(driver, "demonstrateStepScreenshots", "loginPage");
        
        // Verify we're on the login page
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/sign_in"), "Not redirected to login page");
    }
    
    /**
     * Demonstrates UI verification using screenshot comparison
     */
    @Test(groups = {"screenshotDemo"})
    public void demonstrateUIVerification() {
        // Navigate to the homepage
        driver.get(BASE_URL);
        
        // Create a baseline screenshot for the homepage (first run only)
        String baselinePath = UIVerificationUtil.createBaseline(driver, "homepageBaseline");
        
        // In a real test, you would store this baseline path and reuse it in future test runs
        // For demo purposes, we'll compare against the baseline we just created
        boolean matches = UIVerificationUtil.verifyScreenshotWithBaseline(driver, "demonstrateUIVerification", baselinePath);
        
        // This should always be true since we're comparing against a baseline we just created
        Assert.assertTrue(matches, "UI verification failed - homepage doesn't match baseline");
        
        // Capture and verify a specific element
        WebElement logo = driver.findElement(By.cssSelector(".logo"));
        String elementBaseline = UIVerificationUtil.createElementBaseline(driver, logo, "demonstrateUIVerification", "logo");
        
        boolean elementMatches = UIVerificationUtil.verifyElementWithBaseline(
                driver, logo, "demonstrateUIVerification", "logo", elementBaseline);
        
        Assert.assertTrue(elementMatches, "UI verification failed - logo doesn't match baseline");
    }
    
    /**
     * Demonstrates automatic screenshot capture on test failure
     */
    @Test(groups = {"screenshotDemo"})
    public void demonstrateFailureScreenshot() {
        // Navigate to the homepage
        driver.get(BASE_URL);
        
        // This assertion will fail, triggering automatic screenshot capture via CustomTestListener
        Assert.assertEquals(driver.getTitle(), "Incorrect Title", "Title verification failed");
    }
}