package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class MastodonJavaScriptExecutorTest {

    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

        // Maximize browser window and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to Mastodon Social
        driver.get("https://mastodon.social");
    }

    @Test
    public void testJavaScriptExecutorOperations() throws InterruptedException {
        // Scroll down by 1000 pixels
        js.executeScript("window.scrollBy(0,1000);");
        Thread.sleep(2000); // Pause to observe the scroll

        // Retrieve and print the page title
        String title = (String) js.executeScript("return document.title;");
        System.out.println("Page Title: " + title);

        // Retrieve and print the current URL
        String currentURL = (String) js.executeScript("return document.URL;");
        System.out.println("Current URL: " + currentURL);

        // Generate an alert pop-up
        js.executeScript("alert('This is a test alert from JavaScriptExecutor');");
        Thread.sleep(2000); // Wait to view the alert

        // Accept the alert
        driver.switchTo().alert().accept();
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser
        driver.quit();
    }
}
