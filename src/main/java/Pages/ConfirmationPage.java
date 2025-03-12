package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage {
    WebDriver driver;

    By confirmationMessage = By.tagName("h1");
    By confirmationId = By.xpath("//td[contains(text(),'Id')]/following-sibling::td");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to verify if the user is on the Confirmation Page
    public boolean isConfirmationPageDisplayed() {
        return driver.findElement(confirmationMessage).getText().contains("Thank you for your purchase");
    }

    // Method to retrieve and print the confirmation ID
    public String getConfirmationId() {
        String id = driver.findElement(confirmationId).getText();
        System.out.println("Purchase Confirmation ID: " + id);
        return id;
    }
}
