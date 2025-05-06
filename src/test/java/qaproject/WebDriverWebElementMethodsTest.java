package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class WebDriverWebElementMethodsTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void testWebDriverMethods() {
        // get() method
        driver.get("https://mastodon.social");
        Assert.assertTrue(driver.getTitle().contains("Mastodon"), "Page title does not contain 'Mastodon'");

        // getCurrentUrl()
        String currentURL = driver.getCurrentUrl();
        Assert.assertEquals(currentURL, "https://mastodon.social/", "Current URL mismatch");

        // getPageSource()
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("Mastodon"), "Page source does not contain expected text");

        // navigate().to()
        driver.navigate().to("https://mastodon.social/explore");
        Assert.assertTrue(driver.getCurrentUrl().contains("explore"), "Navigate to explore failed");

        // navigate().back()
        driver.navigate().back();
        Assert.assertEquals(driver.getCurrentUrl(), "https://mastodon.social/", "Navigate back failed");

        // navigate().forward()
        driver.navigate().forward();
        Assert.assertTrue(driver.getCurrentUrl().contains("explore"), "Navigate forward failed");

        // navigate().refresh()
        driver.navigate().refresh();
        Assert.assertTrue(driver.getTitle().contains("Mastodon"), "Page not refreshed properly");
    }

    @Test(priority = 2)
    public void testWebElementMethods() throws InterruptedException {
        driver.get("https://mastodon.social");

        WebElement loginLink = driver.findElement(By.linkText("Log in"));
        Assert.assertTrue(loginLink.isDisplayed(), "Login link is not displayed");

        // click()
        loginLink.click();

        // sendKeys() & getAttribute()
        WebElement email = driver.findElement(By.name("user[email]"));
        email.clear(); // just to demo clear()
        email.sendKeys("test@example.com");
        Assert.assertEquals(email.getAttribute("value"), "test@example.com", "Email not entered correctly");

        // password
        WebElement password = driver.findElement(By.name("user[password]"));
        password.sendKeys("test123");

        // isDisplayed()
        Assert.assertTrue(password.isDisplayed(), "Password field is not displayed");

        // isEnabled()
        Assert.assertTrue(password.isEnabled(), "Password field is not enabled");

        // getText() (on button)
        WebElement loginBtn = driver.findElement(By.name("button"));
        Assert.assertEquals(loginBtn.getText(), "Log in", "Login button text mismatch");

        // submit() — optional here since it's not a form but you can still click it
        loginBtn.click();

        // Wait for a second just to observe the behavior
        Thread.sleep(2000);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
