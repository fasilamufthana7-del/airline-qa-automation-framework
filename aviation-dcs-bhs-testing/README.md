# Aviation Domain Testing: DCS, BHS & Flight Disruption (Simulated)

Test design, SQL validation and BDD scenarios for three core airline operations areas:

- **DCS** (Departure Control System): check-in, seat assignment, boarding, flight close
- **BHS** (Baggage Handling System): bag tags, sortation, loading, passenger-bag reconciliation
- **Flight disruption handling**: delays, cancellations, rebooking

> **Transparency note:** real DCS/BHS platforms (Amadeus, Sabre, SITA etc.) are not publicly accessible.
> This module is a **simulation** built for test-design practice. The business rules are simplified and
> the airline code `XY` and all passengers are fictional. This is not production experience.

Part of the [airline-qa-automation-framework](https://github.com/fasilamufthana7-del/airline-qa-automation-framework) portfolio (Selenium, REST Assured, Cucumber, CI).

## What is in this folder

| Path | Purpose |
|---|---|
| `db/schema.sql` | Data model: flights, passengers, bags, bag events, disruptions, rebookings |
| `db/seed.sql` | Sample data with deliberately planted defects |
| `db/validation_queries.sql` | 11 SQL checks (V1-V11); each returns zero rows on healthy data |
| `features/*.feature` | Cucumber/Gherkin scenarios for DCS, BHS and disruption |
| `docs/manual_test_cases.csv` | 21 manual / DB test cases mapped to requirement IDs |
| `docs/sample_defect_reports.md` | Defect reports (with life cycle) for the planted defects |

## Requirements covered (simulated rules)

| ID | Requirement |
|---|---|
| DCS-01 | Checked-in passenger has a seat saved correctly |
| DCS-02 | A seat can be assigned to only one passenger per flight |
| DCS-03 | Boarding pass is issued after check-in |
| DCS-04 | Only checked-in passengers can board |
| DCS-05 | No check-in or boarding after flight close |
| DCS-06 | Check-in cannot exceed flight capacity |
| BHS-01 | Each bag gets a unique 10-digit tag |
| BHS-02 | Bag weight limit is 32 kg |
| BHS-03 | Bag lifecycle: CHECKED_IN -> SORTED -> LOADED |
| BHS-04 | Bag of a no-show passenger is offloaded (reconciliation) |
| BHS-05 | Every bag movement is recorded in the audit trail |
| DIS-01 | Delay updates estimated departure, not scheduled departure |
| DIS-02 | Cancellation puts every passenger into the rebooking queue |
| DIS-03 | Rebooking moves the passenger's bag to the new flight |

## Run the SQL checks

Any SQL database works (H2, MySQL, PostgreSQL, SQLite):

```bash
sqlite3 aviation.db < db/schema.sql
sqlite3 aviation.db < db/seed.sql
sqlite3 aviation.db < db/validation_queries.sql
```

Expected result on the seed data: V1, V5, V6, V8 and V9 each return one row, matching the planted
defects in `docs/sample_defect_reports.md`. V7 uses SQLite `GLOB`; on MySQL use `NOT REGEXP '^[0-9]{10}$'`.

## Status and roadmap

- [x] Requirements, test cases, SQL validation, BDD scenarios, defect reports
- [ ] Mock REST service for DCS/BHS endpoints (check-in, bag scan, flight status)
- [ ] Step definitions: REST Assured for API calls plus JDBC for database assertions
- [ ] Hook into the existing GitHub Actions pipeline and Extent report
