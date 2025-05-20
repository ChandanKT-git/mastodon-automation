package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class MastodonSelectTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://mastodon.social");
    }

    @Test
    public void testFindMultiSelectElements() {
        try {
            // Click login to access more potential select elements
            driver.findElement(By.linkText("Log in")).click();
            
            // Find all select elements on the page
            List<WebElement> selectElements = driver.findElements(By.tagName("select"));
            
            System.out.println("Found " + selectElements.size() + " select elements");
            
            // Analyze each select element
            for (WebElement element : selectElements) {
                Select select = new Select(element);
                
                // Check if it's a multi-select element
                if (select.isMultiple()) {
                    System.out.println("Found multi-select element:");
                    System.out.println("- ID: " + element.getAttribute("id"));
                    System.out.println("- Name: " + element.getAttribute("name"));
                    
                    // Get all options
                    List<WebElement> options = select.getOptions();
                    System.out.println("- Number of options: " + options.size());
                    
                    // Test multi-select functionality if options exist
                    if (options.size() >= 2) {
                        // Select first two options
                        select.selectByIndex(0);
                        select.selectByIndex(1);
                        
                        // Verify selections
                        List<WebElement> selectedOptions = select.getAllSelectedOptions();
                        Assert.assertEquals(selectedOptions.size(), 2, 
                            "Should have 2 selected options");
                        
                        // Deselect all options
                        select.deselectAll();
                        Assert.assertEquals(select.getAllSelectedOptions().size(), 0, 
                            "All options should be deselected");
                    }
                } else {
                    System.out.println("Found single-select element:");
                    System.out.println("- ID: " + element.getAttribute("id"));
                    System.out.println("- Name: " + element.getAttribute("name"));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error while testing select elements: " + e.getMessage());
        }
    }

    @Test
    public void testSelectOperations() {
        try {
            // Navigate to a page that might contain select elements
            driver.findElement(By.linkText("Log in")).click();
            
            // Find all select elements
            List<WebElement> selectElements = driver.findElements(By.tagName("select"));
            
            for (WebElement element : selectElements) {
                Select select = new Select(element);
                
                // Get all options
                List<WebElement> options = select.getOptions();
                
                if (!options.isEmpty()) {
                    System.out.println("Testing select element operations:");
                    
                    // Test selecting by visible text
                    if (options.get(0).getText().length() > 0) {
                        String firstOptionText = options.get(0).getText();
                        select.selectByVisibleText(firstOptionText);
                        Assert.assertEquals(
                            select.getFirstSelectedOption().getText(),
                            firstOptionText,
                            "Should select first option by visible text"
                        );
                    }
                    
                    // Test selecting by index
                    if (options.size() > 1) {
                        select.selectByIndex(1);
                        Assert.assertEquals(
                            select.getFirstSelectedOption(),
                            options.get(1),
                            "Should select second option by index"
                        );
                    }
                    
                    // Test selecting by value if available
                    String optionValue = options.get(0).getAttribute("value");
                    if (optionValue != null && !optionValue.isEmpty()) {
                        select.selectByValue(optionValue);
                        Assert.assertEquals(
                            select.getFirstSelectedOption().getAttribute("value"),
                            optionValue,
                            "Should select option by value"
                        );
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error while testing select operations: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}