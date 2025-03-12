package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TestListener implements ITestListener {

    private static WebDriver driver;  // Using static to avoid constructor issues

    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());
        ScreenshotUtil.captureScreenshot(driver, result.getName()); // Capture screenshot
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
    }

    // Method to take screenshot on failure
    private void takeScreenshot(String testName) {
        if (driver instanceof TakesScreenshot) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("screenshots/" + testName + ".png"));
                System.out.println("Screenshot saved for failed test: " + testName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
