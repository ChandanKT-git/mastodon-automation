package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class CSSSelectorMastodonTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void testByIdLocator() {
        driver.findElement(By.linkText("Log in")).click();
        WebElement emailInput = driver.findElement(By.id("user_email"));
        Assert.assertTrue(emailInput.isDisplayed(), "Email input using ID is not visible.");
    }

    @Test(priority = 2)
    public void testByNameLocator() {
        driver.findElement(By.linkText("Log in")).click();
        WebElement nameField = driver.findElement(By.name("user[email]"));
        Assert.assertTrue(nameField.isDisplayed(), "Name input using name locator is not visible.");
    }

    @Test(priority = 3)
    public void testByClassNameLocator() {
        WebElement logo = driver.findElement(By.className("logo__img"));
        Assert.assertTrue(logo.isDisplayed(), "Logo using class name is not visible.");
    }

    @Test(priority = 4)
    public void testByTagNameLocator() {
        WebElement h1Tag = driver.findElement(By.tagName("h1"));
        Assert.assertTrue(h1Tag.getText().contains("Mastodon"), "H1 tag text mismatch.");
    }

    @Test(priority = 5)
    public void testByLinkTextLocator() {
        WebElement exploreLink = driver.findElement(By.linkText("Explore"));
        Assert.assertTrue(exploreLink.isDisplayed(), "Explore link is not visible.");
    }

    @Test(priority = 6)
    public void testByPartialLinkTextLocator() {
        WebElement trending = driver.findElement(By.partialLinkText("Trend"));
        Assert.assertTrue(trending.isDisplayed(), "Trending link with partial text not visible.");
    }

    @Test(priority = 7)
    public void testByCssSelectorClass() {
        WebElement button = driver.findElement(By.cssSelector(".button.button--secondary"));
        Assert.assertTrue(button.isDisplayed(), "Button with class CSS selector not found.");
    }

    @Test(priority = 8)
    public void testByCssSelectorAttribute() {
        WebElement input = driver.findElement(By.cssSelector("input[type='search']"));
        Assert.assertTrue(input.isDisplayed(), "Input with attribute selector not found.");
    }

    @Test(priority = 9)
    public void testByCssSelectorDescendant() {
        WebElement span = driver.findElement(By.cssSelector("header span"));
        Assert.assertTrue(span.isDisplayed(), "Span as descendant is not visible.");
    }

    @Test(priority = 10)
    public void testByCssSelectorDirectChild() {
        WebElement nav = driver.findElement(By.cssSelector("header > nav"));
        Assert.assertTrue(nav.isDisplayed(), "Direct child nav is not visible.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
