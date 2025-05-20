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

public class MastodonAdvancedXPathTest {

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
    public void testStartsWithXPath() {
        WebElement loginButton = driver.findElement(By.xpath("//a[starts-with(@class, 'button') and contains(text(), 'Log in')]"));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        loginButton.click();
        
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement emailField = driver.findElement(By.xpath("//input[starts-with(@id, 'user_')]"));
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible using starts-with XPath");
    }

    @Test(priority = 2)
    public void testChildAxisXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement form = driver.findElement(By.xpath("//form[@id='new_user']"));
        List<WebElement> childInputs = form.findElements(By.xpath("child::div//input"));
        
        Assert.assertTrue(childInputs.size() >= 2, "Form should have at least 2 input fields using child axis");
        
        WebElement submitButton = form.findElement(By.xpath("child::div//button[@type='submit']"));
        Assert.assertTrue(submitButton.isDisplayed(), "Submit button should be visible using child axis");
    }

    @Test(priority = 3)
    public void testMultipleConditionsXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password' and @type='password' and @required]"));
        Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible using multiple conditions");
        
        WebElement rememberMeCheckbox = driver.findElement(By.xpath("//input[@type='checkbox' and @name='user[remember_me]']"));
        Assert.assertTrue(rememberMeCheckbox.isDisplayed(), "Remember me checkbox should be visible using multiple conditions");
    }

    @Test(priority = 4)
    public void testPositionBasedXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement firstInputField = driver.findElement(By.xpath("(//form[@id='new_user']//input)[1]"));
        Assert.assertTrue(firstInputField.isDisplayed(), "First input field should be visible using position-based XPath");
        
        WebElement lastInputField = driver.findElement(By.xpath("(//form[@id='new_user']//input)[last()]"));
        Assert.assertTrue(lastInputField.isDisplayed(), "Last input field should be visible using position-based XPath");
    }

    @Test(priority = 5)
    public void testAncestorAxisXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement formElement = driver.findElement(By.xpath("//input[@id='user_password']/ancestor::form"));
        
        Assert.assertEquals(formElement.getAttribute("id"), "new_user", "Form should be found using ancestor axis");
    }

    @Test(priority = 6)
    public void testOrConditionXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        List<WebElement> inputFields = driver.findElements(By.xpath("//input[@id='user_email' or @id='user_password']"));
        Assert.assertEquals(inputFields.size(), 2, "Should find exactly 2 input fields using OR condition");
    }

    @Test(priority = 7)
    public void testDescendantAxisXPath() {
        driver.findElement(By.xpath("//a[contains(text(), 'Log in')]")).click();
        wait.until(ExpectedConditions.urlContains("sign_in"));
        
        WebElement mainContainer = driver.findElement(By.xpath("//main"));
        List<WebElement> allInputs = mainContainer.findElements(By.xpath("descendant::input"));
        
        Assert.assertTrue(allInputs.size() >= 3, "Main container should have at least 3 input descendants");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}