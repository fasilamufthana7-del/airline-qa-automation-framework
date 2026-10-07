@bhs
Feature: Baggage Handling System - tagging, sorting and reconciliation

  @BHS-01 @BHS-03 @smoke
  Scenario: Checked bag gets a unique 10-digit tag and moves through the lifecycle
    Given passenger "Amina Khan" is checked in on flight "XY101"
    When she checks in 1 bag weighing 18.5 kg
    Then a bag tag of exactly 10 digits should be generated
    And the bag status should be "CHECKED_IN"
    When the bag is scanned at the sorter
    Then the bag status should be "SORTED"
    When the bag is loaded onto the aircraft
    Then the bag status should be "LOADED"
    And the bag audit trail should contain "CHECKED_IN", "SORTED" and "LOADED" events in order

  @BHS-02 @boundary
  Scenario Outline: Bag weight limit of 32 kg
    When a passenger checks in a bag weighing <weight> kg
    Then the bag should be "<result>"

    Examples:
      | weight | result   |
      | 31.9   | accepted |
      | 32.0   | accepted |
      | 32.1   | rejected |

  @BHS-04 @critical
  Scenario: Bag of a no-show passenger is offloaded (passenger-bag reconciliation)
    Given passenger "Sara Ali" checked in with 1 bag that is already loaded
    And the passenger does not board before the flight closes
    When flight close processing runs
    Then the passenger status should be "NO_SHOW"
    And the bag status should be "OFFLOADED"
    And no bag with status "LOADED" should belong to a non-boarded passenger

  @BHS-05
  Scenario: Missing sorter scan is detected
    Given a bag has a "LOADED" event but no "SORTED" event
    When the audit-trail validation runs
    Then the bag should be flagged as "MISHANDLED"
