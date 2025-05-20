package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class MastodonComplexXPathTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get("https://mastodon.social");
    }

    @Test(priority = 1)
    public void testCombinedAxesXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@id='user_password']/preceding::input[@type='email']"));
        
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be found using preceding axis");
        
        WebElement rememberMeLabel = driver.findElement(By.xpath("//input[@id='user_remember_me']/following-sibling::label"));
        Assert.assertTrue(rememberMeLabel.getText().contains("Remember me"), "Remember me label should be found using following-sibling axis");
    }

    @Test(priority = 2)
    public void testNestedXPathExpressions() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement formContainer = driver.findElement(By.xpath("//div[contains(//h1/text(), 'Sign in')]/following::form"));
        Assert.assertTrue(formContainer.isDisplayed(), "Form container should be found using nested XPath");
        
        WebElement submitButton = driver.findElement(By.xpath("//div[contains(//button/text(), 'Log in')]/ancestor::form"));
        Assert.assertNotNull(submitButton, "Form should be found using nested XPath with ancestor");
    }

    @Test(priority = 3)
    public void testXPathWithIndexing() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        List<WebElement> formDivs = driver.findElements(By.xpath("//form[@id='new_user']/div"));
        Assert.assertTrue(formDivs.size() > 1, "Form should have multiple div elements");
        
        WebElement secondDiv = driver.findElement(By.xpath("(//form[@id='new_user']/div)[2]"));
        Assert.assertTrue(secondDiv.isDisplayed(), "Second div in form should be found using indexing");
    }

    @Test(priority = 4)
    public void testXPathWithMultipleAttributes() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement emailInput = driver.findElement(By.xpath("//input[@type='email' and @required and @id='user_email']"));
        Assert.assertTrue(emailInput.isDisplayed(), "Email input should be found using multiple attributes");
        
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(), 'Log in')]"));
        Assert.assertTrue(submitButton.isDisplayed(), "Submit button should be found using multiple attributes");
    }

    @Test(priority = 5)
    public void testXPathWithTextFunctions() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement exactTextElement = driver.findElement(By.xpath("//a[text()='Forgot your password?']"));
        Assert.assertTrue(exactTextElement.isDisplayed(), "Element with exact text should be found");
        
        WebElement partialTextElement = driver.findElement(By.xpath("//button[contains(text(), 'Log')]"));
        Assert.assertTrue(partialTextElement.isDisplayed(), "Element with partial text should be found");
    }

    @Test(priority = 6)
    public void testXPathWithNormalize() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement element = driver.findElement(By.xpath("//label[normalize-space(text())='Email address']"));
        Assert.assertTrue(element.isDisplayed(), "Element should be found using normalize-space function");
    }

    @Test(priority = 7)
    public void testXPathWithSelfAxis() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement sameElement = passwordField.findElement(By.xpath("self::input"));
        
        Assert.assertEquals(passwordField.getAttribute("id"), sameElement.getAttribute("id"), 
                          "Self axis should return the same element");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}