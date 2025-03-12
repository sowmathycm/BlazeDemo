package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class FlightsPage {
    WebDriver driver;

    // Locator for flight prices & Choose button
    By flightRows = By.xpath("//table/tbody/tr"); // Selects all rows in the table
    By priceColumn = By.xpath("//table/tbody/tr/td[6]"); // Price column (6th column)
    By chooseButtonColumn = By.xpath("//table/tbody/tr/td[1]/input"); // "Choose This Flight" button

    public FlightsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to select the cheapest flight dynamically
    public void selectCheapestFlight() {
        List<WebElement> prices = driver.findElements(priceColumn);
        List<WebElement> chooseButtons = driver.findElements(chooseButtonColumn);

        double minPrice = Double.MAX_VALUE;
        int minIndex = -1;

        // Find the cheapest flight
        for (int i = 0; i < prices.size(); i++) {
            double price = Double.parseDouble(prices.get(i).getText().replace("$", "").trim());
            if (price < minPrice) {
                minPrice = price;
                minIndex = i;
            }
        }

        // Click "Choose This Flight" for the cheapest flight
        if (minIndex != -1) {
            System.out.println("Selecting the cheapest flight with price: $" + minPrice);
            chooseButtons.get(minIndex).click();
        }
    }

    // Method to verify navigation to the Purchase Page
    public boolean isOnPurchasePage() {
        return driver.getTitle().contains("Purchase");
    }
}
