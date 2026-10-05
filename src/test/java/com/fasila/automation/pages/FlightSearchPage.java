package com.fasila.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the BlazeDemo homepage — the "From" / "To" city dropdowns
 * and the Find Flights button.
 */
public class FlightSearchPage {

    private final WebDriver driver;

    private final By fromPortDropdown = By.name("fromPort");
    private final By toPortDropdown = By.name("toPort");
    private final By findFlightsButton = By.cssSelector("input.btn.btn-primary");

    public FlightSearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectDepartureCity(String city) {
        new Select(driver.findElement(fromPortDropdown)).selectByVisibleText(city);
    }

    public void selectDestinationCity(String city) {
        new Select(driver.findElement(toPortDropdown)).selectByVisibleText(city);
    }

    public ChooseFlightPage clickFindFlights() {
        driver.findElement(findFlightsButton).click();
        return new ChooseFlightPage(driver);
    }
}
