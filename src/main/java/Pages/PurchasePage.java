package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.regex.Pattern;

public class PurchasePage {
    WebDriver driver;

    By totalCostField = By.xpath("//p[contains(text(),'Total Cost')]");
    By purchaseButton = By.xpath("//input[@value='Purchase Flight']");

    public PurchasePage(WebDriver driver) {
        this.driver = driver;
    }

    // ✅ Method to check if 'Total Cost' is displayed in xxx.xx format
    public boolean verifyTotalCostFormat() {
        WebElement totalCostElement = driver.findElement(totalCostField);
        String totalCostText = totalCostElement.getText();

        // Extract price using regex
        Pattern pattern = Pattern.compile("\\d+\\.\\d{2}");
        return pattern.matcher(totalCostText).find();
    }

    // ✅ Method to click 'Purchase Flight' button
    public void clickPurchaseFlight() {
        driver.findElement(purchaseButton).click();
    }
}
