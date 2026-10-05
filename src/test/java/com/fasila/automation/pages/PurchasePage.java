package com.fasila.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the purchase/passenger-details page (purchase.php).
 * BlazeDemo doesn't validate these fields strictly, which is fine — the
 * point of this page for us is exercising the "fill a form, submit" flow,
 * a pattern you'll reuse constantly in real airline booking flows
 * (passenger details, payment details, etc.).
 */
public class PurchasePage {

    private final WebDriver driver;

    private final By nameField = By.id("inputName");
    private final By addressField = By.id("address");
    private final By cityField = By.id("city");
    private final By stateField = By.id("state");
    private final By zipCodeField = By.id("zipCode");
    private final By creditCardNumberField = By.id("creditCardNumber");
    private final By nameOnCardField = By.id("nameOnCard");
    private final By purchaseButton = By.cssSelector("input.btn.btn-primary");

    public PurchasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterPassengerDetails(String name, String address, String city,
                                       String state, String zipCode) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(stateField).sendKeys(state);
        driver.findElement(zipCodeField).sendKeys(zipCode);
    }

    public void enterPaymentDetails(String cardNumber, String nameOnCard) {
        driver.findElement(creditCardNumberField).sendKeys(cardNumber);
        driver.findElement(nameOnCardField).sendKeys(nameOnCard);
    }

    public ConfirmationPage clickPurchaseFlight() {
        driver.findElement(purchaseButton).click();
        return new ConfirmationPage(driver);
    }
}
