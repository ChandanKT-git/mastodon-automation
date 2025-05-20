package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class XPathMastodonTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void testAbsoluteXPathLoginButton() {
        WebElement loginBtn = driver.findElement(By.xpath("/html/body/div[1]/div/header/div/nav/div/a[1]"));
        Assert.assertTrue(loginBtn.isDisplayed(), "Login button is not visible.");
        loginBtn.click();
    }

    @Test(priority = 2)
    public void testRelativeXPathSearchBox() {
        WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search']"));
        Assert.assertTrue(searchBox.isDisplayed(), "Search box is not present.");
        searchBox.sendKeys("technology");
    }

    @Test(priority = 3)
    public void testTextXPathExplore() {
        WebElement explore = driver.findElement(By.xpath("//span[text()='Explore']"));
        Assert.assertTrue(explore.isDisplayed(), "Explore link is not found.");
        explore.click();
    }

    @Test(priority = 4)
    public void testContainsTextXPathTrending() {
        WebElement trending = driver.findElement(By.xpath("//span[contains(text(),'Trending')]"));
        Assert.assertTrue(trending.isDisplayed(), "Trending link is not found.");
        trending.click();
    }

    @Test(priority = 5)
    public void testParentAxisXPathEmailInput() throws InterruptedException {
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        Thread.sleep(2000);
        WebElement emailInput = driver.findElement(By.xpath("//label[text()='Email address']/parent::div/input"));
        Assert.assertTrue(emailInput.isDisplayed(), "Email input not found via parent axis.");
        emailInput.sendKeys("sample@test.com");
    }

    @Test(priority = 6)
    public void testFollowingSiblingXPathPassword() throws InterruptedException {
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        Thread.sleep(2000);
        WebElement password = driver.findElement(By.xpath("//label[text()='Password']/following-sibling::input"));
        Assert.assertTrue(password.isDisplayed(), "Password input not found via following-sibling.");
        password.sendKeys("samplePassword");
    }

    @Test(priority = 7)
    public void testPrecedingSiblingXPathLabel() throws InterruptedException {
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        Thread.sleep(2000);
        WebElement label = driver.findElement(By.xpath("//input[@type='password']/preceding-sibling::label"));
        Assert.assertEquals(label.getText(), "Password", "Password label is incorrect.");
    }

    @Test(priority = 8)
    public void testFollowingAxisXPathFromHeader() {
        WebElement header = driver.findElement(By.xpath("//h1[contains(text(),'Mastodon')]"));
        WebElement firstButton = driver.findElement(By.xpath("//h1[contains(text(),'Mastodon')]/following::button[1]"));
        Assert.assertTrue(firstButton.isDisplayed(), "No button found after header.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
