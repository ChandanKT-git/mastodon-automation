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

public class MastodonFormInteractionTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get("https://mastodon.social/auth/sign_in");
    }

    @Test(priority = 1)
    public void testLoginFormValidation() {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='user_email']"))); 
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        
        loginButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'flash-message') and contains(@class, 'alert')]"))); 
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed for empty form");
        
        emailField.sendKeys("invalid-email");
        passwordField.sendKeys("password123");
        loginButton.click();
        
        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'error') and @for='user_email']"))); 
        Assert.assertTrue(emailError.isDisplayed(), "Email validation error should be displayed");
        
        emailField.clear();
        passwordField.clear();
    }

    @Test(priority = 2)
    public void testRememberMeCheckbox() {
        WebElement rememberMeCheckbox = driver.findElement(By.xpath("//input[@id='user_remember_me']"));
        
        Assert.assertFalse(rememberMeCheckbox.isSelected(), "Remember me checkbox should be unchecked by default");
        
        rememberMeCheckbox.click();
        
        Assert.assertTrue(rememberMeCheckbox.isSelected(), "Remember me checkbox should be checked after clicking");
        
        rememberMeCheckbox.click();
        
        Assert.assertFalse(rememberMeCheckbox.isSelected(), "Remember me checkbox should be unchecked after clicking again");
    }

    @Test(priority = 3)
    public void testPasswordVisibilityToggle() {
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement visibilityToggle = driver.findElement(By.xpath("//button[contains(@class, 'password-toggle')]"));
        
        passwordField.sendKeys("TestPassword123");
        
        Assert.assertEquals(passwordField.getAttribute("type"), "password", 
                          "Password should be hidden initially");
        
        visibilityToggle.click();
        
        Assert.assertEquals(passwordField.getAttribute("type"), "text", 
                          "Password should be visible after toggle");
        
        visibilityToggle.click();
        
        Assert.assertEquals(passwordField.getAttribute("type"), "password", 
                          "Password should be hidden again after second toggle");
    }

    @Test(priority = 4)
    public void testForgotPasswordLink() {
        WebElement forgotPasswordLink = driver.findElement(By.xpath("//a[text()='Forgot your password?']"));
        forgotPasswordLink.click();
        
        wait.until(ExpectedConditions.urlContains("password/new"));
        
        Assert.assertTrue(driver.getCurrentUrl().contains("password/new"), 
                         "Should navigate to password reset page");
        
        WebElement resetForm = driver.findElement(By.xpath("//form[@id='new_user']"));
        Assert.assertTrue(resetForm.isDisplayed(), "Password reset form should be displayed");
    }

    @Test(priority = 5)
    public void testAbsoluteXPathLoginForm() {
        WebElement loginForm = driver.findElement(By.xpath("/html/body/div/main/div/div/div/div/form"));
        Assert.assertTrue(loginForm.isDisplayed(), "Login form should be visible using absolute XPath");
        
        List<WebElement> formInputs = loginForm.findElements(By.xpath("/html/body/div/main/div/div/div/div/form//input"));
        Assert.assertTrue(formInputs.size() >= 3, "Form should contain at least 3 input fields");
    }
    
    @Test(priority = 6)
    public void testParentAxisXPath() {
        WebElement emailLabel = driver.findElement(By.xpath("//label[contains(text(), 'Email')]"));
        WebElement emailInput = driver.findElement(By.xpath("//label[contains(text(), 'Email')]/parent::div//input"));
        
        Assert.assertTrue(emailLabel.isDisplayed(), "Email label should be visible");
        Assert.assertTrue(emailInput.isDisplayed(), "Email input found using parent axis should be visible");
        
        emailInput.sendKeys("test@example.com");
        Assert.assertEquals(emailInput.getAttribute("value"), "test@example.com", "Input should contain entered text");
    }
    
    @Test(priority = 7)
    public void testFollowingSiblingXPath() {
        WebElement passwordLabel = driver.findElement(By.xpath("//label[contains(text(), 'Password')]"));
        WebElement passwordInput = driver.findElement(By.xpath("//label[contains(text(), 'Password')]/following-sibling::input"));
        
        Assert.assertTrue(passwordLabel.isDisplayed(), "Password label should be visible");
        Assert.assertTrue(passwordInput.isDisplayed(), "Password input found using following-sibling axis should be visible");
    }
    
    @Test(priority = 8)
    public void testContainsXPath() {
        WebElement signInHeading = driver.findElement(By.xpath("//h1[contains(text(), 'Sign in')]"));
        Assert.assertTrue(signInHeading.isDisplayed(), "Sign in heading should be visible");
        
        WebElement oauthButton = driver.findElement(By.xpath("//button[contains(@class, 'button') and contains(@class, 'oauth')]"));
        Assert.assertTrue(oauthButton.isDisplayed(), "OAuth button should be visible using contains XPath");
    }
    
    @Test(priority = 9)
    public void testPrecedingAxisXPath() {
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='user_password']"));
        WebElement precedingLabel = driver.findElement(By.xpath("//input[@id='user_password']/preceding::label[1]"));
        
        Assert.assertTrue(precedingLabel.getText().contains("Password"), "Label preceding password field should contain 'Password'");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}