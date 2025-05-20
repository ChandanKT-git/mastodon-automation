package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class MastodonAlertFrameTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://mastodon.social");
    }

    @Test
    public void testCheckAlertsPresence() {
        try {
            // Click login to trigger potential alerts
            driver.findElement(By.linkText("Log in")).click();
            
            // Try to trigger an alert by submitting empty form
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']"
            )));
            loginButton.click();
            
            // Check if any alert is present
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                System.out.println("Alert found with text: " + alert.getText());
                alert.accept();
            } catch (TimeoutException e) {
                System.out.println("No JavaScript alerts are present on the page");
            }
        } catch (Exception e) {
            System.out.println("Error while checking alerts: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFramesPresence() {
        try {
            // Get all iframes on the page
            java.util.List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            
            if (iframes.isEmpty()) {
                System.out.println("No iframes found on the current page");
                return;
            }
            
            // Print information about each iframe
            for (WebElement iframe : iframes) {
                System.out.println("Found iframe with:");
                System.out.println("- ID: " + iframe.getAttribute("id"));
                System.out.println("- Name: " + iframe.getAttribute("name"));
                System.out.println("- Source: " + iframe.getAttribute("src"));
                
                // Try to switch to the iframe
                try {
                    driver.switchTo().frame(iframe);
                    System.out.println("Successfully switched to iframe");
                    // Switch back to default content
                    driver.switchTo().defaultContent();
                } catch (Exception e) {
                    System.out.println("Could not switch to iframe: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error while checking frames: " + e.getMessage());
        }
    }

    @Test
    public void testHandleAlerts() {
        try {
            // Execute JavaScript to create different types of alerts
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Simple alert
            js.executeScript("alert('This is a simple alert');");
            Alert simpleAlert = wait.until(ExpectedConditions.alertIsPresent());
            Assert.assertEquals(simpleAlert.getText(), "This is a simple alert");
            simpleAlert.accept();
            
            // Confirmation alert
            js.executeScript("confirm('This is a confirmation alert');");
            Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
            Assert.assertEquals(confirmAlert.getText(), "This is a confirmation alert");
            confirmAlert.dismiss();
            
            // Prompt alert
            js.executeScript("prompt('This is a prompt alert', 'Default text');");
            Alert promptAlert = wait.until(ExpectedConditions.alertIsPresent());
            Assert.assertEquals(promptAlert.getText(), "This is a prompt alert");
            promptAlert.sendKeys("Test input");
            promptAlert.accept();
            
        } catch (Exception e) {
            System.out.println("Error while handling alerts: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}