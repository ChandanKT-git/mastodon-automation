package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

@Test(groups = {"xpath", "mastodon"})
public class XPathMastodonTest {

    WebDriver driver;
    WebDriverWait wait;
    private static final int WAIT_TIMEOUT = 10;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        // Use explicit waits instead of implicit waits for better control
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_TIMEOUT));
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));
    }

    @Test(priority = 1, groups = {"xpath", "absolute"})
    public void testAbsoluteXPathLoginButton() {
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div/header/div/nav/div/a[1]")));
            Assert.assertTrue(loginBtn.isDisplayed(), "Login button is not visible.");
            loginBtn.click();
        } catch (TimeoutException e) {
            Assert.fail("Login button not found within timeout period: " + e.getMessage());
        }
    }

    @Test(priority = 2, groups = {"xpath", "relative"})
    public void testRelativeXPathSearchBox() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Search']"))); 
            Assert.assertTrue(searchBox.isDisplayed(), "Search box is not present.");
            searchBox.sendKeys("technology");
        } catch (TimeoutException e) {
            Assert.fail("Search box not found within timeout period: " + e.getMessage());
        }
    }

    @Test(priority = 3, groups = {"xpath", "text"})
    public void testTextXPathExplore() {
        try {
            WebElement explore = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='Explore']"))); 
            Assert.assertTrue(explore.isDisplayed(), "Explore link is not found.");
            explore.click();
            wait.until(ExpectedConditions.urlContains("explore"));
        } catch (TimeoutException e) {
            Assert.fail("Explore link not found or navigation failed: " + e.getMessage());
        }
    }

    @Test(priority = 4, groups = {"xpath", "contains"})
    public void testContainsTextXPathTrending() {
        try {
            WebElement trending = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'Trending')]"))); 
            Assert.assertTrue(trending.isDisplayed(), "Trending link is not found.");
            trending.click();
            // Wait for the trending page to load
            wait.until(ExpectedConditions.urlContains("trending"));
        } catch (TimeoutException e) {
            Assert.fail("Trending link not found or navigation failed: " + e.getMessage());
        }
    }

    @Test(priority = 5, groups = {"xpath", "parent-axis"})
    public void testParentAxisXPathEmailInput() {
        try {
            // Click login button and wait for page to load
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Log in']")));
            loginButton.click();
            
            // Wait for login page and find email input using parent axis
            wait.until(ExpectedConditions.urlContains("sign_in"));
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Email address']/parent::div/input")));
                
            Assert.assertTrue(emailInput.isDisplayed(), "Email input not found via parent axis.");
            emailInput.sendKeys("sample@test.com");
            
            // Verify input value
            Assert.assertEquals(emailInput.getAttribute("value"), "sample@test.com", 
                "Email input value does not match expected.");
        } catch (TimeoutException e) {
            Assert.fail("Element not found within timeout period: " + e.getMessage());
        }
    }

    @Test(priority = 6, groups = {"xpath", "following-sibling"})
    public void testFollowingSiblingXPathPassword() {
        try {
            // Click login button and wait for page to load
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Log in']")));
            loginButton.click();
            
            // Wait for login page and find password input using following-sibling axis
            wait.until(ExpectedConditions.urlContains("sign_in"));
            WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Password']/following-sibling::input")));
                
            Assert.assertTrue(password.isDisplayed(), "Password input not found via following-sibling.");
            password.sendKeys("samplePassword");
            
            // Verify input type
            Assert.assertEquals(password.getAttribute("type"), "password", 
                "Password field should have type 'password'.");
        } catch (TimeoutException e) {
            Assert.fail("Element not found within timeout period: " + e.getMessage());
        }
    }

    @Test(priority = 7, groups = {"xpath", "preceding-sibling"})
    public void testPrecedingSiblingXPathLabel() {
        try {
            // Click login button and wait for page to load
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Log in']")));
            loginButton.click();
            
            // Wait for login page and find label using preceding-sibling axis
            wait.until(ExpectedConditions.urlContains("sign_in"));
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='password']/preceding-sibling::label")));
                
            Assert.assertEquals(label.getText(), "Password", "Password label is incorrect.");
            Assert.assertTrue(label.isDisplayed(), "Password label should be visible.");
        } catch (TimeoutException e) {
            Assert.fail("Element not found within timeout period: " + e.getMessage());
        }
    }

    @Test(priority = 8, groups = {"xpath", "following-axis"})
    public void testFollowingAxisXPathFromHeader() {
        try {
            // Find header element
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Mastodon')]"))); 
            Assert.assertTrue(header.isDisplayed(), "Mastodon header is not visible.");
            
            // Find first button after header using following axis
            WebElement firstButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Mastodon')]/following::button[1]")));
                
            Assert.assertTrue(firstButton.isDisplayed(), "No button found after header.");
            
            // Verify button is clickable
            Assert.assertTrue(firstButton.isEnabled(), "Button after header should be enabled.");
        } catch (TimeoutException e) {
            Assert.fail("Element not found within timeout period: " + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
