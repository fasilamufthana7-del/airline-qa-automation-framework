package com.fasila.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the final confirmation/receipt page shown after purchase.
 */
public class ConfirmationPage {

    private final WebDriver driver;

    private final By confirmationHeading = By.xpath("//h1[contains(text(), 'Thank you')]");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPurchaseConfirmed() {
        return driver.findElements(confirmationHeading).size() > 0;
    }
}
