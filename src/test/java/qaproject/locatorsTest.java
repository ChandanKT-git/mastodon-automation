package qaproject;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class locatorsTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://mastodon.social/");
    }

    @Test(priority = 1)
    public void clickLoginButton() {
        WebElement loginButton = driver.findElement(By.xpath("//a[@href='/auth/sign_in']/span"));
        loginButton.click();
        Assert.assertTrue(driver.getCurrentUrl().contains("sign_in"), "Not navigated to login page");
    }

    @Test(priority = 2)
    public void loginTest() throws InterruptedException {
        driver.findElement(By.xpath("//a[@href='/auth/sign_in']/span")).click();

        WebElement emailInput = driver.findElement(By.xpath("//input[@id='user_email']"));
        Assert.assertTrue(emailInput.isDisplayed(), "Email input box is not displayed");

        WebElement passwordInput = driver.findElement(By.xpath("//input[@id='user_password']"));
        Assert.assertTrue(passwordInput.isDisplayed(), "Password input box is not displayed");

        emailInput.sendKeys("chandukt29092004@gmail.com");
        passwordInput.sendKeys("Chan@QA24");

        WebElement submitButton = driver.findElement(By.xpath("//input[@name='commit']"));
        submitButton.click();

        Thread.sleep(3000);  // You can replace this with proper wait

        Assert.assertTrue(driver.getCurrentUrl().contains("/web"), "Login failed or not redirected properly");
    }

    @Test(priority = 3)
    public void parentAxisTest() {
        driver.findElement(By.xpath("//a[@href='/auth/sign_in']/span")).click();
        WebElement loginSpan = driver.findElement(By.xpath("//a[@href='/auth/sign_in']/span"));
        WebElement parentAnchor = loginSpan.findElement(By.xpath("parent::a"));
        Assert.assertEquals(parentAnchor.getTagName(), "a", "Parent is not <a> tag");
    }

    @Test(priority = 4)
    public void siblingTest() {
        driver.findElement(By.xpath("//a[@href='/auth/sign_in']/span")).click();
        WebElement emailLabel = driver.findElement(By.xpath("//label[@for='user_email']"));
        WebElement siblingInput = emailLabel.findElement(By.xpath("following-sibling::input"));
        Assert.assertTrue(siblingInput.isDisplayed(), "Sibling input not found");
    }

    @Test(priority = 5)
    public void followingElementTest() {
        WebElement h1Tag = driver.findElement(By.xpath("//h1"));
        WebElement followingDiv = h1Tag.findElement(By.xpath("following::div"));
        Assert.assertTrue(followingDiv.isDisplayed(), "Following div not found after H1");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
