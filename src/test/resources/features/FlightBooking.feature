Feature: Flight Booking
  As a BlazeDemo customer
  I want to search for, choose, and purchase a flight
  So that I can complete a booking end to end

  Scenario: Successfully book a flight from Boston to London
    Given I am on the BlazeDemo homepage
    When I search for a flight from "Boston" to "London"
    Then I should see a list of available flights
    When I choose the first available flight
    And I enter my passenger and payment details
    And I confirm the purchase
    Then my booking should be confirmed
