package com.fasila.automation.stepdefinitions;

import com.fasila.automation.hooks.Hooks;
import com.fasila.automation.pages.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class FlightBookingSteps {

    private FlightSearchPage searchPage;
    private ChooseFlightPage choosePage;
    private PurchasePage purchasePage;
    private ConfirmationPage confirmationPage;

    @Given("I am on the BlazeDemo homepage")
    public void i_am_on_the_blazedemo_homepage() {
        searchPage = new FlightSearchPage(Hooks.driver);
    }

    @When("I search for a flight from {string} to {string}")
    public void i_search_for_a_flight_from_to(String from, String to) {
        searchPage.selectDepartureCity(from);
        searchPage.selectDestinationCity(to);
        choosePage = searchPage.clickFindFlights();
    }

    @Then("I should see a list of available flights")
    public void i_should_see_a_list_of_available_flights() {
        Assert.assertTrue(choosePage.isFlightListDisplayed(),
            "Expected a list of flight results, but none was shown.");
    }

    @When("I choose the first available flight")
    public void i_choose_the_first_available_flight() {
        purchasePage = choosePage.chooseFirstFlight();
    }

    @And("I enter my passenger and payment details")
    public void i_enter_my_passenger_and_payment_details() {
        purchasePage.enterPassengerDetails("Fasila Mufthana", "123 Main St", "Abu Dhabi", "AZ", "00000");
        purchasePage.enterPaymentDetails("4111111111111111", "Fasila Mufthana");
    }

    @And("I confirm the purchase")
    public void i_confirm_the_purchase() {
        confirmationPage = purchasePage.clickPurchaseFlight();
    }

    @Then("my booking should be confirmed")
    public void my_booking_should_be_confirmed() {
        Assert.assertTrue(confirmationPage.isPurchaseConfirmed(),
            "Expected the purchase confirmation page, but it didn't appear.");
    }
}
