package qaproject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;

import java.time.Duration;

public class MastodonExceptionHandlingTests {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testMissingElementHandling() {
        try {
            driver.get("https://mastodon.social/auth/sign_in");

            // Incorrect ID on purpose
            WebElement fakeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wrong_email_id")));
            fakeInput.sendKeys("test@example.com");

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("❌ Element not found or timed out: " + e.getMessage());
        }
    }

    // ✅ Test 2: Handle ElementClickInterceptedException with scroll
    @Test
    public void testClickInterceptedHandling() {
        try {
            driver.get("https://mastodon.social/auth/sign_in");

            WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_email")));
            WebElement password = driver.findElement(By.id("user_password"));

            email.sendKeys("fake@example.com");
            password.sendKeys("wrongpass");

            WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginBtn);
            loginBtn.click();

        } catch (ElementClickInterceptedException e) {
            System.out.println("❌ Click intercepted: " + e.getMessage());
        } catch (TimeoutException e) {
            System.out.println("❌ Login button not found in time: " + e.getMessage());
        }
    }

    @Test
    public void testAlertPresence() {
        try {
            driver.get("https://mastodon.social/auth/sign_in");

            driver.findElement(By.id("user_email")).sendKeys("fake@example.com");
            driver.findElement(By.id("user_password")).sendKeys("wrongpass");
            driver.findElement(By.xpath("//button[@type='submit']")).click();

            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            System.out.println("⚠️ Alert says: " + alert.getText());
            alert.accept();

        } catch (NoAlertPresentException e) {
            System.out.println("❌ No alert present: " + e.getMessage());
        } catch (TimeoutException e) {
            System.out.println("❌ Alert did not appear in time.");
        }
    }
}
