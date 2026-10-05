package com.fasila.automation.tests;

import com.fasila.automation.base.BaseTest;
import com.fasila.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlightBookingTest extends BaseTest {

    @Test
    public void shouldCompleteFlightBookingEndToEnd() {
        FlightSearchPage searchPage = new FlightSearchPage(driver);
        searchPage.selectDepartureCity("Boston");
        searchPage.selectDestinationCity("London");

        ChooseFlightPage choosePage = searchPage.clickFindFlights();
        Assert.assertTrue(choosePage.isFlightListDisplayed(),
            "Expected a list of flight results, but none was shown.");

        PurchasePage purchasePage = choosePage.chooseFirstFlight();
        purchasePage.enterPassengerDetails("Fasila Mufthana", "123 Main St", "Abu Dhabi", "AZ", "00000");
        purchasePage.enterPaymentDetails("4111111111111111", "Fasila Mufthana");

        ConfirmationPage confirmationPage = purchasePage.clickPurchaseFlight();
        Assert.assertTrue(confirmationPage.isPurchaseConfirmed(),
            "Expected the purchase confirmation page, but it didn't appear.");
    }
}
