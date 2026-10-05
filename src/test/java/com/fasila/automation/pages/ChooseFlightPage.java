package com.fasila.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Page Object for the flight results page (reserve.php). BlazeDemo shows a
 * table of flight options, each with its own "Choose This Flight" button.
 */
public class ChooseFlightPage {

    private final WebDriver driver;

    private final By chooseFlightButtons = By.cssSelector("input.btn.btn-small");
    private final By resultsTable = By.cssSelector("table.table");

    public ChooseFlightPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isFlightListDisplayed() {
        return driver.findElements(resultsTable).size() > 0;
    }

    /** Picks the first flight in the list — good enough for a demo/learning scenario. */
    public PurchasePage chooseFirstFlight() {
        List<org.openqa.selenium.WebElement> buttons = driver.findElements(chooseFlightButtons);
        buttons.get(0).click();
        return new PurchasePage(driver);
    }
}
