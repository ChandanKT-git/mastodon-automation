package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class SynchronizationTests {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void usingThreadSleep() throws InterruptedException {
        driver.get("https://mastodon.social");
        Thread.sleep(3000); // Static wait
        WebElement login = driver.findElement(By.linkText("Log in"));
        login.click();
    }

    @Test(priority = 2)
    public void usingImplicitWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://mastodon.social");

        // Even if this takes time to load, implicit wait will handle it
        WebElement login = driver.findElement(By.linkText("Log in"));
        login.click();
    }

    @Test(priority = 3)
    public void usingExplicitWait() {
        driver.get("https://mastodon.social");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log in")));
        login.click();
    }

    @Test(priority = 4)
    public void usingFluentWait() {
        driver.get("https://mastodon.social");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(20))
            .pollingEvery(Duration.ofSeconds(3))
            .ignoring(NoSuchElementException.class);

        WebElement login = fluentWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.linkText("Log in"));
            }
        });

        login.click();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
