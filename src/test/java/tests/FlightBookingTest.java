package tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import Pages.*;

import utils.ExcelUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.TestListener;


public class FlightBookingTest {

    WebDriver driver;
    HomePage homePage;
    FlightsPage flightsPage;
    PurchasePage purchasePage;
    ConfirmationPage confirmationPage;


    protected WebDriverWait wait;

    @BeforeClass
    public void initializedriver() {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().browserVersion("134.0.6998.89").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    @Test
    public void bookFlightTest() throws IOException, InterruptedException {
        driver.get("https://blazedemo.com/index.php");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        homePage = new HomePage(driver);
        flightsPage = new FlightsPage(driver);
        purchasePage = new PurchasePage(driver);
        confirmationPage = new ConfirmationPage(driver);
        TestListener.setDriver(driver);

        //  First, run the test with default cities
        System.out.println("🔄 Running test for default cities: Mexico City → London");
        bookFlight("Mexico City", "London");

        //  Now, read departure & destination cities from Excel and run dynamically
        List<List<String>> cityPairs = ExcelUtils.readExcelFile("TestData.xlsx");

        for (List<String> cityPair : cityPairs) {
            String departureCity = cityPair.get(0);
            String destinationCity = cityPair.get(1);
            System.out.println("🔄 Running test for: " + departureCity + " → " + destinationCity);
            bookFlight(departureCity, destinationCity);
        }

    }

    private void bookFlight(String departureCity, String destinationCity) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Navigate back to the homepage before starting each test run
        driver.get("https://blazedemo.com/index.php");

       // Verifying Home Page Title
        Assert.assertTrue(homePage.verifyHomePageTitle(), "Home page heading mismatch!");
        takeScreenshot("HomePage_Verification");

        // Verifying 'Destination of the Week' Link
        Assert.assertTrue(homePage.verifyDestinationLinkIsDisplayed(), "Destination link is not displayed!");
        takeScreenshot("DestinationLink_Verification");

        // Get the current URL before clicking the link
        String originalUrl = driver.getCurrentUrl();

        // Clicking the 'Destination of the Week' link
        homePage.clickDestinationLink();

        // Wait for the URL to change
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(originalUrl)));

        // Verify the new page URL contains "vacation"
        Assert.assertTrue(homePage.verifyNewPageUrlContainsVacation(), "Navigation did not happen as expected!");
        takeScreenshot("VacationPage_Verification");


        //Navigate back to the homepage
        driver.navigate().back();

       //Select Departure City

        homePage.selectDepartureCity(departureCity);

        // Select Destination City

        homePage.selectDestinationCity(destinationCity);
        // Click 'Find Flights' Button

        homePage.clickFindFlights();
        // Pause execution for 3 seconds to view the success message
        Thread.sleep(3000);
        wait.until(ExpectedConditions.urlContains("reserve"));
        flightsPage.selectCheapestFlight();


        // Verify navigation to the Purchase Page
        wait.until(ExpectedConditions.urlContains("purchase"));
        Assert.assertTrue(flightsPage.isOnPurchasePage(), "Did not navigate to the Purchase Page!");
        takeScreenshot("FlightsPage_Verification");
        Thread.sleep(3000);

        // Verify 'Total Cost' format
        Assert.assertTrue(purchasePage.verifyTotalCostFormat(), "Total Cost is not in xxx.xx format!");
        takeScreenshot("TotalCost_Verification");


        // Click 'Purchase Flight'
        purchasePage.clickPurchaseFlight();
        Thread.sleep(3000);
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);

       // Verify if user is on Confirmation Page
        Assert.assertTrue(confirmationPage.isConfirmationPageDisplayed(), "Purchase Confirmation Page is NOT displayed!");
        takeScreenshot("ConfirmationPage_Verification");

      // Retrieve and store Confirmation ID
        String purchaseId = confirmationPage.getConfirmationId();
        takeScreenshot("ConfirmationID_Saved");

    }


    private void takeScreenshot(String fileName) {
        if (driver == null) {
            System.err.println("WebDriver is null. Screenshot cannot be taken.");
            return;
        }

        if (!(driver instanceof TakesScreenshot)) {
            System.err.println("WebDriver does not support screenshots.");
            return;
        }

        try {
            // Handle multiple windows (if applicable)
            for (String windowHandle : driver.getWindowHandles()) {
                driver.switchTo().window(windowHandle);
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenshotDir = new File("screenshots");

            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                System.err.println(" Failed to create screenshots directory.");
                return;
            }

            File destFile = new File(screenshotDir, fileName + ".png");
            FileUtils.copyFile(srcFile, destFile);


        } catch (IOException e) {
            System.err.println(" Failed to take screenshot: " + e.getMessage());
            e.printStackTrace(); // Print full error details
        } catch (Exception e) {
            System.err.println("Unexpected error while taking screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
