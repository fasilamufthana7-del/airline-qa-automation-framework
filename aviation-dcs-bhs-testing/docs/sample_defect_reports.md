# Sample Defect Reports (illustrative)

These are written against the **seeded defects** in `db/seed.sql`. They show the defect report format and life cycle
(New -> Assigned -> Fixed -> Retested -> Closed / Reopened). They are practice artifacts, not real production defects.

## DEF-001 - Loaded bag belongs to a no-show passenger
| Field | Value |
|---|---|
| Module / Requirement | BHS / BHS-04 |
| Severity / Priority | Critical / High |
| Environment | Simulated DCS+BHS database |
| Steps to reproduce | 1. Check in pax with 1 bag 2. Load bag 3. Pax does not board 4. Run flight close 5. Run query V5 |
| Expected | Bag status OFFLOADED, pax NO_SHOW |
| Actual | Bag 0229123458 remains LOADED while pax IJ56KL is NO_SHOW |
| Impact | Unaccompanied bag on aircraft (security risk) |
| Test case | TC-013 |

## DEF-002 - Overbooking not blocked at check-in
| Field | Value |
|---|---|
| Module / Requirement | DCS / DCS-06 |
| Severity / Priority | High / High |
| Steps to reproduce | 1. Flight XY101 capacity 3 2. Check in 4 passengers 3. Run query V1 |
| Expected | 4th check-in rejected |
| Actual | 4 passengers checked in on capacity 3 |
| Test case | TC-006 |

## DEF-003 - Passenger on cancelled flight has no rebooking record
| Field | Value |
|---|---|
| Module / Requirement | Disruption / DIS-02 |
| Severity / Priority | High / High |
| Steps to reproduce | 1. Cancel flight XY303 2. Run query V9 |
| Expected | Every passenger rebooked or offloaded |
| Actual | Passenger CD56EF (Rahman) has no rebooking |
| Test case | TC-018 |
