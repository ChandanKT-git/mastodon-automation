package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class MastodonActionsTest {

    WebDriver driver;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testHoverOverExplore() {
        // Hover over the 'Explore' link
        WebElement exploreLink = driver.findElement(By.xpath("//a[@href='/explore']"));
        actions.moveToElement(exploreLink).perform();

        // Verify that the 'Explore' link is displayed
        Assert.assertTrue(exploreLink.isDisplayed(), "Explore link is not visible.");
    }

    @Test
    public void testRightClickOnExplore() {
        // Right-click (context click) on the 'Explore' link
        WebElement exploreLink = driver.findElement(By.xpath("//a[@href='/explore']"));
        actions.contextClick(exploreLink).perform();

        // Additional verification can be added here if a context menu appears
    }

    @Test
    public void testDoubleClickOnExplore() {
        // Double-click on the 'Explore' link
        WebElement exploreLink = driver.findElement(By.xpath("//a[@href='/explore']"));
        actions.doubleClick(exploreLink).perform();

        // Additional verification can be added here if double-click triggers an action
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
