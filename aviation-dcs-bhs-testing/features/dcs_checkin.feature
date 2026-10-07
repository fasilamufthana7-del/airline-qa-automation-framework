@dcs
Feature: Departure Control System - check-in, seating and boarding
  # NOTE: business rules below belong to a SIMULATED DCS used for test-design practice.

  Background:
    Given flight "XY202" from "AUH" to "COK" is open for check-in

  @DCS-03 @smoke
  Scenario: Passenger checks in and receives a boarding pass
    Given passenger "Priya Menon" with PNR "QR90ST" is not checked in
    When the agent checks in the passenger and assigns seat "3A"
    Then the passenger status should be "CHECKED_IN"
    And a boarding pass should be issued for seat "3A"

  @DCS-02 @negative
  Scenario: Same seat cannot be assigned to two passengers
    Given seat "3A" is already assigned to another passenger on the flight
    When the agent tries to assign seat "3A" to passenger "John Mathew"
    Then the system should reject the request with a "seat already taken" error
    And no duplicate seat record should exist in the database

  @DCS-04 @negative
  Scenario: Passenger who has not checked in cannot board
    Given passenger "John Mathew" is "NOT_CHECKED"
    When the gate agent scans the passenger for boarding
    Then boarding should be denied
    And the boarding status should remain "NOT_BOARDED"

  @DCS-05 @negative
  Scenario: Check-in is blocked after the flight is closed
    Given the flight status is "CLOSED"
    When the agent tries to check in a new passenger
    Then the system should reject the request with a "flight closed" error

  @DCS-06 @boundary
  Scenario Outline: Check-in respects flight capacity
    Given a flight with capacity <capacity> and <checked_in> passengers already checked in
    When another passenger tries to check in
    Then the result should be "<result>"

    Examples:
      | capacity | checked_in | result   |
      | 3        | 2          | accepted |
      | 3        | 3          | rejected |
