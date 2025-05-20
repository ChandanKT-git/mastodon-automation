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
import java.util.logging.Logger;

public class MastodonSelectClassTest {
    private static final Logger LOGGER = Logger.getLogger(MastodonSelectClassTest.class.getName());
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testSingleSelectByIndex() {
        try {
            // Navigate to Mastodon sign up page where language dropdown exists
            driver.get("https://mastodon.social/auth/sign_up");
            
            // Find the language dropdown (assuming it's a <select> element)
            WebElement languageDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[contains(@id, 'locale') or contains(@name, 'locale')]"));
            
            // Create Select object
            Select select = new Select(languageDropdown);
            
            // Verify dropdown is enabled before interaction
            Assert.assertTrue(languageDropdown.isEnabled(), "Language dropdown should be enabled");
            
            // Select by index (e.g., selecting the second option)
            select.selectByIndex(1);
            
            // Verify selection
            WebElement selectedOption = select.getFirstSelectedOption();
            LOGGER.info("Selected option by index: " + selectedOption.getText());
            Assert.assertNotNull(selectedOption, "An option should be selected");
            
        } catch (Exception e) {
            LOGGER.severe("Error in testSingleSelectByIndex: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void testSingleSelectByValue() {
        try {
            // Navigate to Mastodon sign up page
            driver.get("https://mastodon.social/auth/sign_up");
            
            // Find the language dropdown
            WebElement languageDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[contains(@id, 'locale') or contains(@name, 'locale')]"));
            
            // Create Select object
            Select select = new Select(languageDropdown);
            
            // Select by value (e.g., selecting English)
            select.selectByValue("en");
            
            // Verify selection
            WebElement selectedOption = select.getFirstSelectedOption();
            LOGGER.info("Selected option by value: " + selectedOption.getText());
            Assert.assertEquals(selectedOption.getAttribute("value"), "en", 
                             "Option with value 'en' should be selected");
            
        } catch (Exception e) {
            LOGGER.severe("Error in testSingleSelectByValue: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void testSingleSelectByVisibleText() {
        try {
            // Navigate to Mastodon sign up page
            driver.get("https://mastodon.social/auth/sign_up");
            
            // Find the language dropdown
            WebElement languageDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[contains(@id, 'locale') or contains(@name, 'locale')]"));
            
            // Create Select object
            Select select = new Select(languageDropdown);
            
            // Select by visible text
            select.selectByVisibleText("English");
            
            // Verify selection
            WebElement selectedOption = select.getFirstSelectedOption();
            LOGGER.info("Selected option by visible text: " + selectedOption.getText());
            Assert.assertEquals(selectedOption.getText(), "English", 
                             "Option with text 'English' should be selected");
            
        } catch (Exception e) {
            LOGGER.severe("Error in testSingleSelectByVisibleText: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void testGetAllOptions() {
        try {
            // Navigate to Mastodon sign up page
            driver.get("https://mastodon.social/auth/sign_up");
            
            // Find the language dropdown
            WebElement languageDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[contains(@id, 'locale') or contains(@name, 'locale')]"));
            
            // Create Select object
            Select select = new Select(languageDropdown);
            
            // Get all options
            List<WebElement> allOptions = select.getOptions();
            
            // Verify options list
            Assert.assertTrue(allOptions.size() > 0, "Dropdown should have at least one option");
            LOGGER.info("Total number of options: " + allOptions.size());
            
            // Log first few options for verification
            for (int i = 0; i < Math.min(5, allOptions.size()); i++) {
                LOGGER.info("Option " + i + ": " + allOptions.get(i).getText());
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error in testGetAllOptions: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void testMultiSelectOperations() {
        try {
            // Navigate to a page with a multi-select dropdown (for demonstration)
            // Note: This is a simulated test as Mastodon might not have a multi-select dropdown
            driver.get("https://mastodon.social");
            
            // Create a multi-select dropdown using JavaScript for testing purposes
            ((JavascriptExecutor) driver).executeScript(
                "const select = document.createElement('select'); " +
                "select.id = 'multiSelect'; " +
                "select.multiple = true; " +
                "select.innerHTML = '<option value=\\'option1\\'>Option 1</option>' + " +
                "'<option value=\\'option2\\'>Option 2</option>' + " +
                "'<option value=\\'option3\\'>Option 3</option>'; " +
                "document.body.appendChild(select);");
            
            // Wait for the created element
            WebElement multiSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("multiSelect")));
            
            // Create Select object
            Select select = new Select(multiSelect);
            
            // Verify it's a multi-select
            Assert.assertTrue(select.isMultiple(), "Should be a multi-select dropdown");
            
            // Select multiple options
            select.selectByIndex(0);
            select.selectByValue("option2");
            
            // Verify multiple selections
            List<WebElement> selectedOptions = select.getAllSelectedOptions();
            Assert.assertEquals(selectedOptions.size(), 2, "Two options should be selected");
            
            // Deselect by index
            select.deselectByIndex(0);
            selectedOptions = select.getAllSelectedOptions();
            Assert.assertEquals(selectedOptions.size(), 1, "One option should remain selected after deselect");
            
            // Deselect all
            select.deselectAll();
            selectedOptions = select.getAllSelectedOptions();
            Assert.assertEquals(selectedOptions.size(), 0, "No options should be selected after deselectAll");
            
        } catch (Exception e) {
            LOGGER.severe("Error in testMultiSelectOperations: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    public void testIsSelectedAndIsEnabled() {
        try {
            // Navigate to Mastodon sign up page
            driver.get("https://mastodon.social/auth/sign_up");
            
            // Find the language dropdown
            WebElement languageDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[contains(@id, 'locale') or contains(@name, 'locale')]"));
            
            // Check if dropdown is enabled
            boolean isEnabled = languageDropdown.isEnabled();
            LOGGER.info("Is dropdown enabled: " + isEnabled);
            Assert.assertTrue(isEnabled, "Dropdown should be enabled");
            
            // Create Select object
            Select select = new Select(languageDropdown);
            
            // Get all options
            List<WebElement> options = select.getOptions();
            
            // Select an option
            select.selectByIndex(1);
            
            // Check if the selected option is selected
            WebElement selectedOption = select.getFirstSelectedOption();
            boolean isSelected = selectedOption.isSelected();
            LOGGER.info("Is option selected: " + isSelected);
            Assert.assertTrue(isSelected, "Option should be selected");
            
            // Check if a non-selected option is not selected
            if (options.size() > 2) {
                WebElement nonSelectedOption = options.get(2); // Get a different option
                boolean isNonSelectedOptionSelected = nonSelectedOption.isSelected();
                LOGGER.info("Is non-selected option selected: " + isNonSelectedOptionSelected);
                Assert.assertFalse(isNonSelectedOptionSelected, "Non-selected option should not be selected");
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error in testIsSelectedAndIsEnabled: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}