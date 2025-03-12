package Pages;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.*;
import java.time.Duration;
import java.util.Set;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;


    By findFlightsButton = By.xpath("//input[@value='Find Flights']");
    By pageTitle = By.tagName("h1");
    By destinationLink = By.partialLinkText("destination of the week");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Method to verify home page title text
    public boolean verifyHomePageTitle() {
        WebElement headingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        return headingElement.getText().contains("Welcome to the Simple Travel Agency!");
    }


    // Method to verify 'Destination of the Week' link is displayed
    public boolean verifyDestinationLinkIsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(destinationLink)).isDisplayed();
    }

    // Method to click 'Destination of the Week' link
    public void clickDestinationLink() {
        wait.until(ExpectedConditions.elementToBeClickable(destinationLink)).click();
    }

    // Method to verify if the new page URL contains "vacation"
    public boolean verifyNewPageUrlContainsVacation() {
        return driver.getCurrentUrl().contains("vacation");
    }

    public void selectDepartureCity(String departureCity) {
        WebElement departureDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='fromPort']")));
        Select selectDeparture = new Select(departureDropdown);
        selectDeparture.selectByVisibleText(departureCity);
    }

    public void selectDestinationCity(String destinationCity) {
        WebElement destinationDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='toPort']")));
        Select selectDestination = new Select(destinationDropdown);
        selectDestination.selectByVisibleText(destinationCity);
    }
    public void clickFindFlights() {
        WebElement findFlightsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit']")));
        findFlightsButton.click();
    }

}

