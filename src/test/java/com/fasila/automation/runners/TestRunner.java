package com.fasila.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * This is the class you actually run — not the .feature file directly.
 * It tells Cucumber where to find:
 *  - features: your .feature files (the plain-English scenarios)
 *  - glue: the packages containing your step definitions AND hooks
 *          (both LoginSteps and Hooks must be listed here, or Cucumber
 *          won't find the @Before/@After/@Given/@When/@Then methods)
 *  - plugin: produces an HTML report you can open in a browser afterwards,
 *            at target/cucumber-reports/report.html
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.fasila.automation.stepdefinitions", "com.fasila.automation.hooks"},
    plugin = {"pretty", "html:target/cucumber-reports/report.html"},
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
