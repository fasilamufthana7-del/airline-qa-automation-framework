# ✈️ Airline QA Automation Framework

![CI](https://github.com/fasilamufthana7-del/airline-qa-automation-framework/actions/workflows/ci.yml/badge.svg) ![Java](https://img.shields.io/badge/Java-17-orange) ![Selenium](https://img.shields.io/badge/Selenium-4.24-green) ![Cucumber](https://img.shields.io/badge/Cucumber-BDD-brightgreen) ![REST Assured](https://img.shields.io/badge/REST%20Assured-API%20Testing-blue)
   **New:** [Aviation domain testing (DCS, BHS, disruption)](aviation-dcs-bhs-testing/README.md) - test cases, SQL validation and BDD scenarios.
An end-to-end, AI-assisted test automation framework built around an airline/flight-booking workflow — combining UI automation, BDD, REST API testing, and AI-generated test scenarios in a single Java/Maven project, with a CI/CD pipeline that runs the full suite on every push.

Built as a hands-on portfolio project while job-hunting for QA roles in the UAE's aviation sector.

## Why airline, and why this exists

Most public QA portfolio projects are built against the same handful of banking demo sites. This one is built around a flight-booking journey instead — closer to the domain most UAE aviation QA roles (Etihad, Emirates, and others) actually work in day to day.

## What this framework actually does

|Layer|What it covers|Tech|
|-|-|-|
|**UI Automation**|Full flight booking journey: search → choose flight → passenger \& payment details → confirmation|Selenium WebDriver, Page Object Model|
|**BDD**|The same journey expressed in plain-English Gherkin, readable by non-technical stakeholders|Cucumber|
|**API Testing**|Live flight-tracking data validation, including a query scoped to flights near Abu Dhabi|REST Assured, OpenSky Network API|
|**AI-Assisted Test Generation**|Converts a plain-English requirement into a ready-to-use Gherkin feature file|Google Gemini API|
|**Reporting**|HTML test report with pass/fail status and automatic failure screenshots|ExtentReports|
|**CI/CD**|Runs the entire suite headlessly on every push; publishes the report as a build artifact|GitHub Actions|

## AI-assisted test generation — before \& after

One of the differentiators of this framework: instead of only writing test scenarios by hand, `AITestCaseGenerator.java` takes a plain-English requirement and asks Gemini to generate a structured Cucumber feature file from it.

**Input (a requirement, written like a BA or Product Owner would write it):**

> "As a BlazeDemo customer, I want to book a flight by selecting a departure city and a destination city, choosing one of the returned flight options, and entering my passenger and payment details, so that I can complete a purchase. The system should also handle the case where the departure and destination cities are the same, which should not be allowed."

**Output (generated automatically):**

```gherkin
Feature: Flight Booking
  ...
  Scenario: Successfully book a flight with valid details
    When the user selects "Boston" as the departure city
    ...

  Scenario: Prevent flight booking when departure and destination cities are the same
    When the user selects "Paris" as the departure city
    And the user selects "Paris" as the destination city
    ...
```

This demonstrates how AI can accelerate the requirement-to-test-case translation step — a genuinely time-consuming part of QA work — without replacing a tester's judgment on what to actually automate.

## Project structure

```
src/test/java/com/fasila/automation/
├── base/              # Shared WebDriver setup/teardown
├── pages/             # Page Object Model classes
├── tests/             # Plain TestNG tests
├── stepdefinitions/   # Cucumber step definitions
├── hooks/             # Cucumber lifecycle hooks
├── runners/           # Cucumber-TestNG runner
├── api/               # REST Assured API tests
├── ai/                # AI-assisted test case generator (Gemini)
└── listeners/         # ExtentReports integration

src/test/resources/
├── features/          # Gherkin .feature files
└── config.properties  # Local config (gitignored — see .example file)

.github/workflows/     # CI/CD pipeline definition
postman/                # Companion Postman collection for manual API exploration
```

## Running it locally

1. Clone the repo and import as a Maven project (tested in Eclipse).
2. Copy `src/test/resources/config.properties.example` to `config.properties` and add a free Gemini API key from [aistudio.google.com/apikey](https://aistudio.google.com/apikey) (only needed for the AI generator — the UI/API/BDD tests don't require it).
3. Run the full suite: right-click `testng.xml` → Run As → TestNG Suite, or from the command line:

```
   mvn test
   ```

4. Run the Cucumber scenarios specifically: right-click `TestRunner.java` → Run As → TestNG Test.
5. Run the AI generator directly: right-click `AITestCaseGenerator.java` → Run As → Java Application.
6. View the HTML report at `target/extent-reports/`.

## Real-world scope note

This framework targets public demo/practice targets ([BlazeDemo](https://blazedemo.com) for UI, [OpenSky Network](https://opensky-network.org) for live flight data) rather than production airline systems — those sit behind proprietary, licensed platforms (PSS systems like Amadeus Altéa/Sabre Sonic, GDS platforms like Amadeus/Sabre/Travelport) that aren't publicly testable. The framework is built to mirror how testing against those systems is structured in practice: UI + API + BDD layers working together, with CI/CD running the suite automatically. Concepts like PNR validation, fare rule checks, and IROPS (irregular operations — delays, cancellations, rebooking) handling would extend naturally from this foundation in a production context.

## About me

QA Engineer with 4 years of experience in manual and automation testing across insurance and banking domains, now focused on airline/aviation QA opportunities in the UAE. Certifications: ISTQB CTFL v4.0, Microsoft Azure DevOps Engineer Expert (AZ-400), AWS Certified AI Practitioner, Claude Certified Associate Foundations, Selenium 4 WebDriver with Java.

linkedin.com/in/fasilamufthana · fasilamufthana7@gmail.com



