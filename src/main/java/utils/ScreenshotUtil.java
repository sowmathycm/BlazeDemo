package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ScreenshotUtil {

    // Directory to store screenshots
    private static final String SCREENSHOT_DIR = "screenshots/";

    // Method to capture screenshot
    public static void captureScreenshot(WebDriver driver, String testName) {
        // Ensure the driver supports taking screenshots
        if (!(driver instanceof TakesScreenshot)) {
            System.err.println("Driver does not support taking screenshots.");
            return;
        }

        // Create the screenshots directory if it doesn't exist
        File directory = new File(SCREENSHOT_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create screenshot directory: " + SCREENSHOT_DIR);
            return;
        }

        // Define screenshot file path
        String screenshotPath = SCREENSHOT_DIR + testName + ".png";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("Screenshot saved: " + new File(screenshotPath).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}
