package com.fasila.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasila.automation.base.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.format.DateTimeFormatter;

/**
 * A TestNG "listener" is code that runs automatically around every test —
 * you don't call these methods yourself, TestNG calls them for you at each
 * stage (test starts, test passes, test fails, etc). This one builds an
 * ExtentReports HTML report as your tests run.
 *
 * Registered via the <listeners> block in testng.xml, so it applies to
 * EVERY test class in the suite (both FlightBookingTest and
 * OpenSkyApiTest) without needing to change either of those classes.
 */
public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent;
    private static ExtentTest test;

    @Override
    public void onStart(ITestContext context) {
        // IMPORTANT: a TestNG suite with multiple <test> blocks (we have
        // UITests and APITests) calls onStart/onFinish once PER block, not
        // once per suite. Without this null check, APITests' onStart would
        // silently create a second ExtentReports instance pointed at a
        // second file — meaning UITests and APITests would end up in two
        // separate reports instead of one combined one. Guarding on
        // `extent == null` ensures only the FIRST <test> block creates the
        // report; every later block reuses the same instance, so all tests
        // across the whole suite land in a single file.
        if (extent == null) {
            String timestamp = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            ExtentSparkReporter reporter =
                new ExtentSparkReporter("target/extent-reports/report-" + timestamp + ".html");
            reporter.config().setDocumentTitle("Airline QA Automation Report");
            reporter.config().setReportName("Flight Booking & API Test Results");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, result.getThrowable());

        // If this was a UI test (extends BaseTest), grab a screenshot of
        // the browser at the moment of failure and embed it in the report —
        // genuinely useful for debugging later, not just for show.
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) instance).getDriver();
            if (driver != null) {
                try {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.addScreenCaptureFromBase64String(base64Screenshot, "Failure screenshot");
                } catch (Exception e) {
                    test.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test skipped.");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
