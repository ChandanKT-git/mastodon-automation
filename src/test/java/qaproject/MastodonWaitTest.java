package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class MastodonWaitTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testCheckedExceptions() {
        // Demonstrating IOException handling
        Properties props = new Properties();
        try {
            // Attempting to load a non-existent file (checked exception)
            FileInputStream fis = new FileInputStream(new File("nonexistent.properties"));
            props.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("Handled FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Handled IOException: " + e.getMessage());
        }

        // Demonstrating wait instead of Thread.sleep
        driver.get("https://mastodon.social");
        try {
            // Using explicit wait instead of Thread.sleep
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            System.out.println("Used explicit wait instead of Thread.sleep");
        } catch (Exception e) {
            System.out.println("Exception during wait: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void testUncheckedExceptions() {
        driver.get("https://mastodon.social");

        try {
            // Attempting to find a non-existent element (unchecked exception)
            driver.findElement(By.id("nonexistent-element"));
        } catch (NoSuchElementException e) {
            System.out.println("Handled NoSuchElementException: " + e.getMessage());
        }

        try {
            // Attempting to click a null element (unchecked exception)
            WebElement nullElement = null;
            nullElement.click();
        } catch (NullPointerException e) {
            System.out.println("Handled NullPointerException: " + e.getMessage());
        }

        try {
            // Attempting invalid array access (unchecked exception)
            Object[] emptyArray = new Object[0];
            Object item = emptyArray[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Handled ArrayIndexOutOfBoundsException: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void testSeleniumExceptions() {
        driver.get("https://mastodon.social");

        try {
            // Setting a very short timeout to demonstrate TimeoutException
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(100));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@id='nonexistent']"
            )));
        } catch (TimeoutException e) {
            System.out.println("Handled TimeoutException: " + e.getMessage());
        }

        try {
            // Attempting to interact with a stale element
            WebElement loginButton = driver.findElement(By.xpath("//a[contains(text(), 'Log in')]"));
            driver.navigate().refresh();
            loginButton.click(); // This will throw StaleElementReferenceException
        } catch (StaleElementReferenceException e) {
            System.out.println("Handled StaleElementReferenceException: " + e.getMessage());
        }

        try {
            // Attempting to execute invalid JavaScript
            ((JavascriptExecutor) driver).executeScript("invalid javascript code");
        } catch (JavascriptException e) {
            System.out.println("Handled JavascriptException: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void testExplicitWaitInsteadOfSleep() {
        driver.get("https://mastodon.social");
        
        // Using explicit wait instead of Thread.sleep
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Log in')]"))); 
        
        WebElement loginButton = driver.findElement(By.xpath("//a[contains(text(), 'Log in')]"));
        loginButton.click();
        
        // Another explicit wait instead of Thread.sleep
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='user_email']")));
        
        WebElement emailField = driver.findElement(By.xpath("//input[@id='user_email']"));
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible after explicit wait");
    }

    @Test(priority = 5)
    public void testImplicitWait() {
        // Setting implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get("https://mastodon.social");
        
        // Implicit wait will be applied to all element finds
        WebElement loginButton = driver.findElement(By.xpath("//a[contains(text(), 'Log in')]"));
        loginButton.click();
        
        WebElement emailField = driver.findElement(By.xpath("//input[@id='user_email']"));
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible with implicit wait");
        
        // Reset implicit wait to avoid interference with other tests
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    @Test(priority = 6)
    public void testExplicitWaitWithWebDriverWait() {
        driver.get("https://mastodon.social");
        
        // Using WebDriverWait for better control
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Log in')]"
        )));
        loginButton.click();
        
        // Wait for URL change
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        // Wait for email field to be visible and enabled
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//input[@id='user_email']"
        )));
        Assert.assertTrue(emailField.isEnabled(), "Email field should be enabled after explicit wait");
    }

    @Test(priority = 7)
    public void testFluentWait() {
        driver.get("https://mastodon.social");
        
        // Configure FluentWait
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(30))
            .pollingEvery(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);
        
        // Use FluentWait to find and click login button
        WebElement loginButton = fluentWait.until(driver -> {
            return driver.findElement(By.xpath("//a[contains(text(), 'Log in')]"));
        });
        loginButton.click();
        
        // Use FluentWait with custom condition
        WebElement emailField = fluentWait.until(driver -> {
            WebElement element = driver.findElement(By.xpath("//input[@id='user_email']"));
            return element.isDisplayed() && element.isEnabled() ? element : null;
        });
        
        Assert.assertTrue(emailField.isEnabled(), "Email field should be enabled after fluent wait");
    }

    @Test(priority = 8)
    public void testCombinedWaitStrategies() {
        // Set a minimal implicit wait for extreme cases
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        
        driver.get("https://mastodon.social");
        
        // Use explicit wait for specific conditions
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Log in')]"
        )));
        loginButton.click();
        
        // Use FluentWait for complex conditions
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(20))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(NoSuchElementException.class);
        
        WebElement emailField = fluentWait.until(driver -> {
            WebElement element = driver.findElement(By.xpath("//input[@id='user_email']"));
            return element.isDisplayed() && element.isEnabled() ? element : null;
        });
        
        Assert.assertTrue(emailField.isEnabled(), "Email field should be enabled with combined wait strategies");
        
        // Reset implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}