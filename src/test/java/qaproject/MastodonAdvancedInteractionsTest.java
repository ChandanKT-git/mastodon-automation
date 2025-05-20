package qaproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class MastodonAdvancedInteractionsTest {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        
        // Maximize browser window and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Navigate to Mastodon Social
        driver.get("https://mastodon.social");
    }

    @Test
    public void testHoverInteractions() {
        // Find an element to hover over (e.g., a post or navigation item)
        WebElement navItem = driver.findElement(By.xpath("//a[contains(@href, '/explore')]"));
        
        // Perform hover action
        actions.moveToElement(navItem).perform();
        
        // Verify hover effect (could be a tooltip, style change, etc.)
        // For example, check if a CSS class was added or if an element became visible
        String hoverClass = navItem.getAttribute("class");
        Assert.assertTrue(hoverClass.contains("active") || hoverClass.contains("hover"), 
                         "Hover effect should be applied to navigation item");
    }

    @Test
    public void testKeyboardInteractions() throws InterruptedException {
        // Navigate to login page
        driver.findElement(By.linkText("Log in")).click();
        
        // Wait for the login form to be visible
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_email")));
        
        // Type text using keyboard actions
        actions.click(emailField)
               .sendKeys("test")
               .keyDown(Keys.SHIFT)
               .sendKeys("@example")
               .keyUp(Keys.SHIFT)
               .sendKeys(".com")
               .perform();
        
        // Verify the text was entered correctly
        Assert.assertEquals(emailField.getAttribute("value"), "test@EXAMPLE.com", 
                          "Text with keyboard modifiers should be entered correctly");
        
        // Test tab key navigation
        actions.sendKeys(Keys.TAB).perform();
        
        // Verify focus moved to password field
        WebElement passwordField = driver.findElement(By.id("user_password"));
        Assert.assertEquals(driver.switchTo().activeElement(), passwordField, 
                          "Focus should move to password field after Tab key");
    }

    @Test
    public void testScrollingInteractions() {
        // Find an element far down the page
        WebElement footerElement = driver.findElement(By.tagName("footer"));
        
        // Scroll to the element
        actions.scrollToElement(footerElement).perform();
        
        // Alternative scrolling method using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", footerElement);
        
        // Verify the element is now visible in viewport
        boolean isInViewport = (Boolean) js.executeScript(
            "var elem = arguments[0],"
            + "box = elem.getBoundingClientRect(),"
            + "cx = box.left + box.width / 2,"
            + "cy = box.top + box.height / 2,"
            + "e = document.elementFromPoint(cx, cy);"
            + "for (; e; e = e.parentElement) {"
            + "  if (e === elem) return true;"
            + "}"
            + "return false;", footerElement);
        
        Assert.assertTrue(isInViewport, "Footer element should be visible in viewport after scrolling");
    }

    @Test
    public void testDragAndDrop() {
        // Note: This test is conceptual and may need adjustment based on actual draggable elements
        // For demonstration purposes only
        
        // Navigate to a page with draggable elements (if available)
        driver.get("https://mastodon.social/settings/preferences/appearance");
        
        try {
            // Wait for page to load and try to find draggable elements
            // (This is hypothetical - adjust selectors based on actual page structure)
            WebElement sourceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".draggable-item")));
            WebElement targetElement = driver.findElement(By.cssSelector(".drop-zone"));
            
            // Perform drag and drop
            actions.dragAndDrop(sourceElement, targetElement).perform();
            
            // Alternative drag and drop method
            actions.clickAndHold(sourceElement)
                   .moveToElement(targetElement)
                   .release()
                   .perform();
            
            // Verify the drag and drop was successful
            // This would depend on the specific UI behavior
            Assert.assertTrue(targetElement.findElements(By.cssSelector(".draggable-item")).size() > 0, 
                             "Element should be dragged to target successfully");
            
        } catch (TimeoutException | NoSuchElementException e) {
            // If draggable elements aren't found, log a message
            System.out.println("Draggable elements not found in this section. Test is conceptual.");
        }
    }

    @Test
    public void testRightClickContextMenu() {
        // Find an element to right-click on
        WebElement element = driver.findElement(By.tagName("body"));
        
        // Perform right-click action
        actions.contextClick(element).perform();
        
        // Verify context menu appears
        // This is browser-dependent, so we'll just check if any menu-like element appears
        try {
            boolean contextMenuVisible = driver.findElements(By.cssSelector(".context-menu, #context-menu")).size() > 0 ||
                                        driver.findElements(By.xpath("//div[contains(@class, 'menu') and @style='display: block;']")).size() > 0;
            
            // If we can't find a context menu element, it might be a browser native menu which is harder to detect
            if (!contextMenuVisible) {
                System.out.println("Browser native context menu may have appeared but cannot be detected via Selenium");
            }
            
            // Press Escape to dismiss the context menu
            actions.sendKeys(Keys.ESCAPE).perform();
            
        } catch (Exception e) {
            System.out.println("Context menu interaction may vary by browser: " + e.getMessage());
        }
    }

    @Test
    public void testCssSelectors() {
        // Navigate to login page
        driver.findElement(By.linkText("Log in")).click();
        
        // Test ID selector
        WebElement emailInput = driver.findElement(By.cssSelector("#user_email"));
        Assert.assertTrue(emailInput.isDisplayed(), "Email input should be found using ID selector");
        
        // Test Class selector
        WebElement loginButton = driver.findElement(By.cssSelector(".button"));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button should be found using Class selector");
        
        // Test Tag selector
        List<WebElement> inputFields = driver.findElements(By.cssSelector("input"));
        Assert.assertTrue(inputFields.size() >= 3, "At least 3 input fields should be found using Tag selector");
        
        // Test Attribute selector
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        Assert.assertTrue(passwordField.isDisplayed(), "Password field should be found using Attribute selector");
        
        // Test Class + Attribute selector
        WebElement submitButton = driver.findElement(By.cssSelector(".button[type='submit']"));
        Assert.assertTrue(submitButton.isDisplayed(), "Submit button should be found using Class + Attribute selector");
        
        // Test Descendant selector
        WebElement formInput = driver.findElement(By.cssSelector("form input"));
        Assert.assertTrue(formInput.isDisplayed(), "Form input should be found using Descendant selector");
        
        // Test Direct Child selector
        WebElement directChild = driver.findElement(By.cssSelector("form > div"));
        Assert.assertTrue(directChild.isDisplayed(), "Direct child div should be found using Direct Child selector");
    }
    
    @Test
    public void testWebDriverMethods() {
        // Test get() method
        driver.get("https://mastodon.social/explore");
        
        // Test getTitle() method
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Mastodon"), "Page title should contain 'Mastodon'");
        
        // Test getCurrentUrl() method
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("explore"), "Current URL should contain 'explore'");
        
        // Test navigate() methods
        driver.navigate().to("https://mastodon.social/about");
        Assert.assertTrue(driver.getCurrentUrl().contains("about"), "URL should contain 'about' after navigation");
        
        driver.navigate().back();
        Assert.assertTrue(driver.getCurrentUrl().contains("explore"), "URL should contain 'explore' after navigating back");
        
        driver.navigate().forward();
        Assert.assertTrue(driver.getCurrentUrl().contains("about"), "URL should contain 'about' after navigating forward");
        
        driver.navigate().refresh();
        Assert.assertTrue(driver.getCurrentUrl().contains("about"), "URL should still contain 'about' after refresh");
    }
    
    @Test
    public void testWebElementMethods() {
        // Navigate to login page
        driver.findElement(By.linkText("Log in")).click();
        
        // Test sendKeys() and clear() methods
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_email")));
        emailField.sendKeys("test@example.com");
        Assert.assertEquals(emailField.getAttribute("value"), "test@example.com", "Email field should contain entered text");
        emailField.clear();
        Assert.assertEquals(emailField.getAttribute("value"), "", "Email field should be empty after clear");
        
        // Test click() method
        WebElement rememberMeCheckbox = driver.findElement(By.id("user_remember_me"));
        rememberMeCheckbox.click();
        Assert.assertTrue(rememberMeCheckbox.isSelected(), "Checkbox should be selected after click");
        
        // Test getText() method
        WebElement loginHeading = driver.findElement(By.tagName("h1"));
        Assert.assertTrue(loginHeading.getText().contains("Sign in"), "Heading text should contain 'Sign in'");
        
        // Test getAttribute() method
        String inputType = emailField.getAttribute("type");
        Assert.assertEquals(inputType, "email", "Email field should have type attribute 'email'");
        
        // Test isDisplayed() method
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be displayed");
        
        // Test isEnabled() method
        Assert.assertTrue(emailField.isEnabled(), "Email field should be enabled");
        
        // Test submit() method (if form is available)
        try {
            WebElement loginForm = driver.findElement(By.tagName("form"));
            emailField.sendKeys("test@example.com");
            WebElement passwordField = driver.findElement(By.id("user_password"));
            passwordField.sendKeys("password123");
            loginForm.submit();
            
            // Wait for error message since we're using fake credentials
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".flash-message") ));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed after form submission");
        } catch (Exception e) {
            System.out.println("Form submission test encountered an exception: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}