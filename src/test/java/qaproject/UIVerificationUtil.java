package qaproject;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class for UI verification through screenshot comparison
 */
public class UIVerificationUtil {
    
    private static final String COMPARISON_DIR = "test-output/screenshot-comparisons";
    private static final double DEFAULT_THRESHOLD = 0.05; // 5% difference threshold
    
    /**
     * Compares the current UI state with a baseline screenshot
     * 
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @param baselineImagePath Path to the baseline image
     * @return true if images match within threshold, false otherwise
     */
    public static boolean verifyScreenshotWithBaseline(WebDriver driver, String testName, String baselineImagePath) {
        return verifyScreenshotWithBaseline(driver, testName, baselineImagePath, DEFAULT_THRESHOLD);
    }
    
    /**
     * Compares the current UI state with a baseline screenshot using custom threshold
     * 
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @param baselineImagePath Path to the baseline image
     * @param threshold Difference threshold (0.0 - 1.0)
     * @return true if images match within threshold, false otherwise
     */
    public static boolean verifyScreenshotWithBaseline(WebDriver driver, String testName, String baselineImagePath, double threshold) {
        // Take current screenshot
        String currentScreenshotPath = ScreenshotUtil.takeScreenshot(driver, testName, "verification");
        if (currentScreenshotPath == null) {
            return false;
        }
        
        // Compare with baseline
        try {
            return compareImages(new File(baselineImagePath), new File(currentScreenshotPath), testName, threshold);
        } catch (IOException e) {
            System.err.println("Failed to compare images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Compares a specific element with a baseline screenshot
     * 
     * @param driver WebDriver instance
     * @param element WebElement to verify
     * @param testName Name of the test
     * @param elementName Name of the element
     * @param baselineImagePath Path to the baseline image
     * @return true if images match within threshold, false otherwise
     */
    public static boolean verifyElementWithBaseline(WebDriver driver, WebElement element, String testName, 
            String elementName, String baselineImagePath) {
        return verifyElementWithBaseline(driver, element, testName, elementName, baselineImagePath, DEFAULT_THRESHOLD);
    }
    
    /**
     * Compares a specific element with a baseline screenshot using custom threshold
     * 
     * @param driver WebDriver instance
     * @param element WebElement to verify
     * @param testName Name of the test
     * @param elementName Name of the element
     * @param baselineImagePath Path to the baseline image
     * @param threshold Difference threshold (0.0 - 1.0)
     * @return true if images match within threshold, false otherwise
     */
    public static boolean verifyElementWithBaseline(WebDriver driver, WebElement element, String testName, 
            String elementName, String baselineImagePath, double threshold) {
        // Take current element screenshot
        String currentScreenshotPath = ScreenshotUtil.takeElementScreenshot(driver, element, testName, elementName);
        if (currentScreenshotPath == null) {
            return false;
        }
        
        // Compare with baseline
        try {
            return compareImages(new File(baselineImagePath), new File(currentScreenshotPath), 
                    testName + "_" + elementName, threshold);
        } catch (IOException e) {
            System.err.println("Failed to compare element images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Creates a baseline screenshot for future comparisons
     * 
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @return Path to the created baseline image
     */
    public static String createBaseline(WebDriver driver, String testName) {
        return ScreenshotUtil.takeScreenshot(driver, testName, "baseline");
    }
    
    /**
     * Creates a baseline screenshot of a specific element for future comparisons
     * 
     * @param driver WebDriver instance
     * @param element WebElement to capture
     * @param testName Name of the test
     * @param elementName Name of the element
     * @return Path to the created baseline image
     */
    public static String createElementBaseline(WebDriver driver, WebElement element, String testName, String elementName) {
        return ScreenshotUtil.takeElementScreenshot(driver, element, testName, elementName);
    }
    
    /**
     * Compares two images and generates a difference image
     * 
     * @param baselineImage Baseline image file
     * @param currentImage Current image file
     * @param testName Name of the test (used for diff image naming)
     * @param threshold Difference threshold (0.0 - 1.0)
     * @return true if images match within threshold, false otherwise
     * @throws IOException if image processing fails
     */
    private static boolean compareImages(File baselineImage, File currentImage, String testName, double threshold) 
            throws IOException {
        // Create comparison directory if it doesn't exist
        File comparisonDir = new File(COMPARISON_DIR);
        if (!comparisonDir.exists()) {
            comparisonDir.mkdirs();
        }
        
        // Read images
        BufferedImage expectedImage = ImageIO.read(baselineImage);
        BufferedImage actualImage = ImageIO.read(currentImage);
        
        // Check dimensions
        if (expectedImage.getWidth() != actualImage.getWidth() || 
                expectedImage.getHeight() != actualImage.getHeight()) {
            System.out.println("Images have different dimensions. Expected: " + 
                    expectedImage.getWidth() + "x" + expectedImage.getHeight() + 
                    ", Actual: " + actualImage.getWidth() + "x" + actualImage.getHeight());
            
            // Create a simple difference image showing the size difference
            BufferedImage diffImage = new BufferedImage(
                    Math.max(expectedImage.getWidth(), actualImage.getWidth()),
                    Math.max(expectedImage.getHeight(), actualImage.getHeight()),
                    BufferedImage.TYPE_INT_RGB);
            
            // Save the difference image
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String diffImagePath = COMPARISON_DIR + File.separator + testName + "_diff_" + timestamp + ".png";
            ImageIO.write(diffImage, "png", new File(diffImagePath));
            
            return false;
        }
        
        // Compare pixel by pixel and create diff image
        BufferedImage diffImage = new BufferedImage(
                expectedImage.getWidth(),
                expectedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        
        int diffPixels = 0;
        int totalPixels = expectedImage.getWidth() * expectedImage.getHeight();
        
        for (int y = 0; y < expectedImage.getHeight(); y++) {
            for (int x = 0; x < expectedImage.getWidth(); x++) {
                int expectedRGB = expectedImage.getRGB(x, y);
                int actualRGB = actualImage.getRGB(x, y);
                
                if (expectedRGB != actualRGB) {
                    diffPixels++;
                    // Mark the difference pixel in red
                    diffImage.setRGB(x, y, Color.RED.getRGB());
                } else {
                    // Copy the original pixel
                    diffImage.setRGB(x, y, expectedRGB);
                }
            }
        }
        
        // Calculate difference percentage
        double diffPercentage = (double) diffPixels / totalPixels;
        
        // Save the difference image if threshold exceeded
        if (diffPercentage > threshold) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String diffImagePath = COMPARISON_DIR + File.separator + testName + "_diff_" + timestamp + ".png";
            ImageIO.write(diffImage, "png", new File(diffImagePath));
            
            System.out.println("Images differ by " + String.format("%.2f%%", diffPercentage * 100) + 
                    ", which exceeds the threshold of " + String.format("%.2f%%", threshold * 100));
            System.out.println("Difference image saved: " + diffImagePath);
            
            return false;
        }
        
        System.out.println("Images match within threshold. Difference: " + 
                String.format("%.2f%%", diffPercentage * 100));
        return true;
    }
}