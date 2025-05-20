package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.logging.Logger;

public class MastodonCustomExceptionTest {
    private static final Logger LOGGER = Logger.getLogger(MastodonCustomExceptionTest.class.getName());
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

<<<<<<< HEAD
    @Test(priority = 1, groups = {"exceptionTests"})
=======
    @Test(priority = 1)
>>>>>>> 0f9e4d61e27a145c4ebcba9614759011b9f8606a
    public void testNetworkExceptionHandling() {
        try {
            // Attempt to load page with potential network issues
            driver.get("https://mastodon.social");
            
            // Custom network check
            if (!isPageLoadComplete()) {
                throw new CustomNetworkException("Page failed to load completely");
            }
        } catch (WebDriverException e) {
            LOGGER.severe("Network error occurred: " + e.getMessage());
            Assert.fail("Network error: " + e.getMessage());
        } catch (CustomNetworkException e) {
            LOGGER.warning("Custom network error: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

<<<<<<< HEAD
    @Test(priority = 2, groups = {"exceptionTests"})
=======
    @Test(priority = 2)
>>>>>>> 0f9e4d61e27a145c4ebcba9614759011b9f8606a
    public void testElementStateExceptionHandling() {
        try {
            driver.get("https://mastodon.social");
            
            // Find and verify login button state
            WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(), 'Log in')]")
            ));
            
            if (!isElementInteractable(loginButton)) {
                throw new ElementStateException("Login button is not in an interactable state");
            }
            
            loginButton.click();
            
        } catch (ElementStateException e) {
            LOGGER.warning("Element state error: " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (ElementClickInterceptedException e) {
            LOGGER.warning("Click intercepted: " + e.getMessage());
            handleClickInterception(e);
        }
    }

    @Test(priority = 3)
    public void testCustomTimeoutHandling() {
        try {
            driver.get("https://mastodon.social");
            
            // Custom timeout handling for login process
            if (!performLoginWithTimeout()) {
                throw new CustomTimeoutException("Login process exceeded maximum allowed time");
            }
            
        } catch (CustomTimeoutException e) {
            LOGGER.severe("Custom timeout error: " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error during login: " + e.getMessage());
            Assert.fail("Login failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void testRecoveryMechanism() {
        int retryCount = 0;
        int maxRetries = 3;
        
        while (retryCount < maxRetries) {
            try {
                driver.get("https://mastodon.social");
                WebElement loginButton = findLoginButtonWithRetry();
                loginButton.click();
                break;
                
            } catch (Exception e) {
                retryCount++;
                LOGGER.warning("Attempt " + retryCount + " failed: " + e.getMessage());
                
                if (retryCount == maxRetries) {
                    LOGGER.severe("All retry attempts failed");
                    Assert.fail("Failed after " + maxRetries + " attempts");
                }
                
                // Wait before retry
                try {
                    Thread.sleep(1000 * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    LOGGER.warning("Retry wait interrupted");
                }
            }
        }
    }

    // Custom Exceptions
    private static class CustomNetworkException extends Exception {
        public CustomNetworkException(String message) {
            super(message);
        }
    }

    private static class ElementStateException extends Exception {
        public ElementStateException(String message) {
            super(message);
        }
    }

    private static class CustomTimeoutException extends Exception {
        public CustomTimeoutException(String message) {
            super(message);
        }
    }

    // Helper Methods
    private boolean isPageLoadComplete() {
        try {
            return ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isElementInteractable(WebElement element) {
        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private void handleClickInterception(ElementClickInterceptedException e) {
        try {
            WebElement element = driver.findElement(By.xpath("//a[contains(text(), 'Log in')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception ex) {
            LOGGER.severe("Failed to handle click interception: " + ex.getMessage());
        }
    }

    private boolean performLoginWithTimeout() {
        try {
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Log in')]")
            ));
            loginButton.click();
            
            return wait.until(ExpectedConditions.urlContains("sign_in"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement findLoginButtonWithRetry() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Log in')]")
        ));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}