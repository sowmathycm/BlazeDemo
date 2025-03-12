package Pages;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import utils.TestListener;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;


    By findFlightsButton = By.xpath("//input[@value='Find Flights']"); // Update if necessary
    By pageTitle = By.tagName("h1");
    By destinationLink = By.partialLinkText("destination of the week");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(20));
        WebDriverBrowser TestListener;
       // TestListener.setDriver(driver);
    }

    // Method to get the page title
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Method to verify home page title text
    public boolean verifyHomePageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                .getText().contains("Welcome to the Simple Travel Agency!");
    }

    // Method to click "destination of the week" link
    public void clickDestinationLink() {
        wait.until(ExpectedConditions.elementToBeClickable(destinationLink)).click();
    }

    // Method to check if the new tab URL contains "vacation"
    public boolean verifyNewTabUrlContainsVacation() {
        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                boolean urlContainsVacation = driver.getCurrentUrl().contains("vacation");
                driver.close();  // Close new tab
                driver.switchTo().window(parentWindow);  // Switch back to main tab
                return urlContainsVacation;
            }
        }
        return false; // Return false if no new tab was found
    }

        public void selectDepartureCity() {
            System.out.println("Waiting for Departure City option: Mexico City...");

            // Wait for the "Mexico City" option to be present in the dropdown
            WebElement mexicoCityOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='fromPort']/option[contains(text(), 'Mexico City')]")));

            System.out.println("Selecting Departure City: Mexico City");

            // Click the "Mexico City" option directly
            mexicoCityOption.click();

            // Verify selection
            WebElement selectedOption = driver.findElement(By.xpath("//select[@name='fromPort']/option[@selected='selected']"));
            System.out.println("Selected Departure City: " + selectedOption.getText());
        }





        public void selectDestinationCity() {
            System.out.println("Waiting for Destination City option: London...");

            // Wait until the "London" option is present in the dropdown
            WebElement londonOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='toPort']/option[@value='London']")));

            System.out.println("Selecting Destination City: London");

            // Click the "London" option to select it
            londonOption.click();

            // Verify selection
            WebElement selectedOption = driver.findElement(By.xpath("//select[@name='toPort']/option[@selected='selected']"));
            System.out.println("Selected Destination City: " + selectedOption.getText());
        }


    // Method to click "Find Flights" button
    public void clickFindFlights() {
        WebElement findFlights = wait.until(ExpectedConditions.elementToBeClickable(findFlightsButton));
        findFlights.click();
    }


}

