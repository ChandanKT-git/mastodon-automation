package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Set;
import java.util.Iterator;

public class MastodonWindowHandlingTest {
    WebDriver driver;
    WebDriverWait wait;
    String parentWindow;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void testWindowHandling() {
        try {
            // Navigate to Mastodon sign-in page
            driver.get("https://mastodon.social/auth/sign_in");
            parentWindow = driver.getWindowHandle();

            // Open privacy policy in new window using JavaScript
            WebElement privacyLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'privacy policy')]"));
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0].href, '_blank');", privacyLink);

            // Wait for new window and switch to it
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            Set<String> handles = driver.getWindowHandles();
            Iterator<String> it = handles.iterator();

            while (it.hasNext()) {
                String childWindow = it.next();
                if (!childWindow.equals(parentWindow)) {
                    driver.switchTo().window(childWindow);
                    break;
                }
            }

            // Verify privacy policy page
            wait.until(ExpectedConditions.urlContains("privacy-policy"));
            Assert.assertTrue(driver.getCurrentUrl().contains("privacy-policy"), 
                            "Should be on privacy policy page");

            // Close child window and switch back to parent
            driver.close();
            driver.switchTo().window(parentWindow);

            // Verify we're back on the login page
            Assert.assertTrue(driver.getCurrentUrl().contains("sign_in"), 
                            "Should be back on sign in page");

        } catch (Exception e) {
            System.out.println(" Window handling error: " + e.getMessage());
            Assert.fail("Window handling test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void testMultipleWindowHandling() {
        try {
            driver.get("https://mastodon.social/auth/sign_in");
            parentWindow = driver.getWindowHandle();

            // Open multiple links in new windows
            String[] linkTexts = {"privacy policy", "Terms of service"};
            for (String text : linkTexts) {
                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(String.format("//a[contains(text(), '%s')]", text))));
                ((JavascriptExecutor) driver).executeScript("window.open(arguments[0].href, '_blank');", link);
            }

            // Wait for all windows to open
            wait.until(ExpectedConditions.numberOfWindowsToBe(3));
            Set<String> allWindows = driver.getWindowHandles();

            // Switch to each window and verify content
            for (String window : allWindows) {
                driver.switchTo().window(window);
                String currentUrl = driver.getCurrentUrl();

                if (currentUrl.contains("privacy-policy")) {
                    Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h1[contains(text(), 'Privacy Policy')]"))).isDisplayed(),
                        "Privacy Policy heading should be visible");
                } else if (currentUrl.contains("terms")) {
                    Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h1[contains(text(), 'Terms of Service')]"))).isDisplayed(),
                        "Terms of Service heading should be visible");
                }

                // Close child windows but keep parent
                if (!window.equals(parentWindow)) {
                    driver.close();
                }
            }

            // Switch back to parent window
            driver.switchTo().window(parentWindow);
            Assert.assertTrue(driver.getCurrentUrl().contains("sign_in"),
                            "Should be back on sign in page after closing all child windows");

        } catch (Exception e) {
            System.out.println(" Multiple window handling error: " + e.getMessage());
            Assert.fail("Multiple window handling test failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}