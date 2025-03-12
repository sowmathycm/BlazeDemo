package tests;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import Pages.*;

import utils.ExcelUtils;
import utils.ScreenshotUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
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

        // ✅ First, run the test with default cities
        System.out.println("🔄 Running test for default cities: Mexico City → London");
        bookFlight("Mexico City", "London");

        // ✅ Now, read departure & destination cities from Excel and run dynamically
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

        // ✅ Navigate back to the homepage before starting each test run
        driver.get("https://blazedemo.com/index.php");


// ✅ Verify Home Page Title
        String expectedHeading = "Welcome to the Simple Travel Agency!";
        String actualHeading = driver.findElement(By.tagName("h1")).getText();
        Assert.assertEquals(actualHeading, expectedHeading, "Home page heading mismatch!");
        takeScreenshot("HomePage_Verification");
        System.out.println("✅ Home page heading is displayed correctly.");

        // ✅ Verify 'Destination of the Week' Link
        WebElement destinationLink = driver.findElement(By.partialLinkText("destination of the week! The Beach!"));
        Assert.assertTrue(destinationLink.isDisplayed(), "Destination link is not displayed!");
        takeScreenshot("DestinationLink_Verification");
        System.out.println("✅ Destination link is displayed correctly.");

        // Get the current URL before clicking the link
        String originalUrl = driver.getCurrentUrl();
        destinationLink.click();

        // ✅ Wait for the URL to change instead of expecting a new tab
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(originalUrl)));

        // ✅ Verify the new page URL contains "vacation"
        String newUrl = driver.getCurrentUrl();
        Assert.assertTrue(newUrl.contains("vacation"), "Navigation did not happen as expected!");
        takeScreenshot("VacationPage_Verification");

        // ✅ Navigate back to the homepage
        driver.navigate().back();
        System.out.println("🔙 Navigated back to the homepage.");

       //  ✅ Select Departure City
        System.out.println("🔍 Selecting Departure City: " + departureCity + "...");
        WebElement departureDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='fromPort']")));
        Select selectDeparture = new Select(departureDropdown);
        selectDeparture.selectByVisibleText(departureCity);



        // ✅ Verify selected Departure City
//        String selectedDeparture = selectDeparture.getFirstSelectedOption().getText();
//        Assert.assertEquals(selectedDeparture, "Mexico City", " Departure City selection failed!");
//        System.out.println("✅ Departure City selected: " + selectedDeparture);

        // ✅ Select Destination City - London
        System.out.println("🔍 Selecting Destination City: London...");
        WebElement destinationDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='toPort']")));
        Select selectDestination = new Select(destinationDropdown);
        selectDestination.selectByVisibleText(destinationCity);

//        // ✅ Verify selected Destination City
//        String selectedDestination = selectDestination.getFirstSelectedOption().getText();
//        Assert.assertEquals(selectedDestination, "London", "Destination City selection failed!");
//        System.out.println("✅ Destination City selected: " + selectedDestination);

        // ✅ Click 'Find Flights' Button
        System.out.println("➡️ Clicking 'Find Flights' button...");
        WebElement findFlightsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit']")));
        findFlightsButton.click();
        // Pause execution for 3 seconds to view the success message
        Thread.sleep(3000);

        wait.until(ExpectedConditions.urlContains("reserve"));
        System.out.println("✅ Successfully navigated to the flight selection page.");

        flightsPage.selectCheapestFlight();
        System.out.println("✅ Selected the cheapest flight and clicked 'Choose This Flight'.");

        // ✅ Verify navigation to the Purchase Page
        wait.until(ExpectedConditions.urlContains("purchase"));
        Assert.assertTrue(flightsPage.isOnPurchasePage(), "Did not navigate to the Purchase Page!");
        takeScreenshot("FlightsPage_Verification");

        System.out.println("✅ Successfully navigated to the Purchase Page.");
        Thread.sleep(3000);

        // ✅ Verify 'Total Cost' format
        Assert.assertTrue(purchasePage.verifyTotalCostFormat(), "Total Cost is not in xxx.xx format!");
        takeScreenshot("TotalCost_Verification");
        System.out.println("✅ Total Cost is correctly displayed in xxx.xx format.");

        // ✅ Click 'Purchase Flight'
        purchasePage.clickPurchaseFlight();
        System.out.println("➡️ Clicked 'Purchase Flight' button.");

        // Pause execution for 3 seconds to view the success message
        Thread.sleep(3000);

        // Instantiate Confirmation Page
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);

       // Verify if user is on Confirmation Page
        Assert.assertTrue(confirmationPage.isConfirmationPageDisplayed(), "Purchase Confirmation Page is NOT displayed!");
        takeScreenshot("ConfirmationPage_Verification");

      // Retrieve and store Confirmation ID
        String purchaseId = confirmationPage.getConfirmationId();
        System.out.println("Purchase successfully completed. Confirmation ID: " + purchaseId);
        takeScreenshot("ConfirmationID_Saved");



    }
    private void takeScreenshot(String fileName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenshotDir = new File("screenshots");

            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs(); // Create directory if it doesn’t exist
            }

            File destFile = new File(screenshotDir, fileName + ".png");
            Files.copy(srcFile.toPath(), destFile.toPath());
            System.out.println("📸 Screenshot taken: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }

//    public List<String[]> readExcelData(String filePath) {
//        List<String[]> cityPairs = new ArrayList<>();
//        try (FileInputStream fis = new FileInputStream(new File(filePath));
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue; // Skip the header row
//
//                Cell departureCell = row.getCell(0);
//                Cell destinationCell = row.getCell(1);
//
//                if (departureCell != null && destinationCell != null) {
//                    String departureCity = departureCell.getStringCellValue();
//                    String destinationCity = destinationCell.getStringCellValue();
//                    cityPairs.add(new String[]{departureCity, destinationCity});
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return cityPairs;
//    }




    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
