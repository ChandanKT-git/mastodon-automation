package qaproject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import qaproject.util.CSVReader;
import qaproject.util.ExcelReader;

/**
 * Demo test class that demonstrates how to use data-driven testing with
 * Excel and CSV data sources in the Mastodon test suite.
 */
public class DataDrivenDemoTest {
    
    private WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * DataProvider that reads test data from an Excel file.
     * 
     * @return Two-dimensional array of test data
     * @throws IOException If the Excel file cannot be read
     */
    @DataProvider(name = "excelDataProvider")
    public Object[][] getExcelData() throws IOException {
        // Path to the Excel file (create this file with test data before running)
        String excelFilePath = "src/test/resources/testdata/login_data.xlsx";
        
        // Ensure the directory exists
        File directory = new File("src/test/resources/testdata");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Check if file exists and provide guidance if it doesn't
        File excelFile = new File(excelFilePath);
        if (!excelFile.exists()) {
            System.out.println("Excel file not found: " + excelFilePath);
            System.out.println("Please create an Excel file with columns: username, password, expectedResult");
            // Return empty array to avoid test failure during setup
            return new Object[0][0];
        }
        
        // Read data from the "LoginTests" sheet
        return ExcelReader.readSheetAsDataProvider(excelFilePath, "LoginTests");
    }
    
    /**
     * DataProvider that reads test data from a CSV file.
     * 
     * @return Two-dimensional array of test data
     * @throws IOException If the CSV file cannot be read
     */
    @DataProvider(name = "csvDataProvider")
    public Object[][] getCSVData() throws IOException {
        // Path to the CSV file (create this file with test data before running)
        String csvFilePath = "src/test/resources/testdata/search_data.csv";
        
        // Ensure the directory exists
        File directory = new File("src/test/resources/testdata");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Check if file exists and provide guidance if it doesn't
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            System.out.println("CSV file not found: " + csvFilePath);
            System.out.println("Please create a CSV file with columns: searchTerm, expectedResults");
            // Return empty array to avoid test failure during setup
            return new Object[0][0];
        }
        
        // Read data from the CSV file
        return CSVReader.readCSVAsDataProvider(csvFilePath);
    }
    
    /**
     * Example test that uses Excel data for login testing.
     * 
     * @param testData Map containing test data from Excel
     */
    @Test(dataProvider = "excelDataProvider", groups = {"dataTests"})
    public void testLoginWithExcelData(Map<String, String> testData) {
        // Extract test data
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedResult = testData.get("expectedResult");
        
        System.out.println("Running login test with: " + username + ", " + password);
        
        // Navigate to login page (replace with actual URL)
        driver.get("https://example.com/login");
        
        // Perform login (replace with actual selectors)
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
        
        // Verify result (replace with actual verification logic)
        if ("success".equals(expectedResult)) {
            // Check for successful login
            boolean isLoggedIn = driver.getTitle().contains("Dashboard");
            Assert.assertTrue(isLoggedIn, "Login should be successful");
        } else {
            // Check for error message
            boolean hasError = driver.findElement(By.className("error-message")).isDisplayed();
            Assert.assertTrue(hasError, "Error message should be displayed for invalid login");
        }
        
        // Take screenshot for verification
        ScreenshotUtil.takeStepScreenshot(driver, "loginTest", username + "_result");
    }
    
    /**
     * Example test that uses CSV data for search testing.
     * 
     * @param testData Map containing test data from CSV
     */
    @Test(dataProvider = "csvDataProvider", groups = {"dataTests"})
    public void testSearchWithCSVData(Map<String, String> testData) {
        // Extract test data
        String searchTerm = testData.get("searchTerm");
        String expectedResults = testData.get("expectedResults");
        
        System.out.println("Running search test with: " + searchTerm);
        
        // Navigate to search page (replace with actual URL)
        driver.get("https://example.com/search");
        
        // Perform search (replace with actual selectors)
        driver.findElement(By.id("searchBox")).sendKeys(searchTerm);
        driver.findElement(By.id("searchButton")).click();
        
        // Verify results (replace with actual verification logic)
        int expectedResultCount = Integer.parseInt(expectedResults);
        int actualResultCount = driver.findElements(By.className("search-result")).size();
        
        // Take screenshot for verification
        ScreenshotUtil.takeStepScreenshot(driver, "searchTest", searchTerm + "_results");
        
        // Assert that we have at least the expected number of results
        Assert.assertTrue(actualResultCount >= expectedResultCount, 
                "Expected at least " + expectedResultCount + " results, but got " + actualResultCount);
    }
}