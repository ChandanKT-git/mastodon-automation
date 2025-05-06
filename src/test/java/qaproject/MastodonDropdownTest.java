package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class MastodonDropdownTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testCustomDropdown() throws InterruptedException {
        // Example: Interacting with a custom dropdown (e.g., language selector)
        // Note: Adjust the XPath/CSS selectors based on the actual elements

        // Click on the settings or profile menu to reveal dropdown options
        WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Open menu']"));
        menuButton.click();

        // Wait for the dropdown to be visible
        Thread.sleep(2000); // Replace with explicit wait if necessary

        // Click on the desired option within the custom dropdown
        WebElement dropdownOption = driver.findElement(By.xpath("//span[text()='Preferences']"));
        dropdownOption.click();

        // Add assertions or further interactions as needed
        Assert.assertTrue(driver.getCurrentUrl().contains("preferences"), "Failed to navigate to Preferences page.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
