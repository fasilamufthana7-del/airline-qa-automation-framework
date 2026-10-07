@disruption
Feature: Flight disruption handling - delays, cancellations, rebooking

  @DIS-01 @smoke
  Scenario: Delay updates estimated departure
    Given flight "XY202" is scheduled to depart at 14:00
    When a 90-minute delay is recorded with reason "Late inbound aircraft"
    Then the flight status should be "DELAYED"
    And the estimated departure should be 15:30
    And the scheduled departure should remain 14:00

  @DIS-02 @critical
  Scenario: Cancelled flight requires every passenger to be rebooked
    Given flight "XY303" has 2 checked-in passengers
    When the flight is cancelled with reason "Technical issue"
    Then every passenger should appear in the rebooking queue with status "PENDING"
    And check-in and boarding on "XY303" should be blocked

  @DIS-03
  Scenario: Rebooked passenger's bag moves to the new flight
    Given passenger "Lena Fischer" on cancelled flight "XY303" has 1 bag
    When the passenger is rebooked to flight "XY304"
    Then the rebooking status should be "CONFIRMED"
    And the bag should be re-tagged to flight "XY304"
    And the old flight should hold no bag for this passenger
