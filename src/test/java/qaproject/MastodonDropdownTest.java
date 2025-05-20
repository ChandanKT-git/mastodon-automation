package qaproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class MastodonDropdownTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(groups = {"selectionTests"})
    public void testCustomDropdown() {
        // Example: Interacting with a custom dropdown (e.g., language selector)
        // Note: Adjust the XPath/CSS selectors based on the actual elements

        // Click on the settings or profile menu to reveal dropdown options
        WebElement menuButton = driver.findElement(By.xpath("//button[@aria-label='Open menu']"));
        menuButton.click();

        // Wait for the dropdown to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Preferences']")));

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
