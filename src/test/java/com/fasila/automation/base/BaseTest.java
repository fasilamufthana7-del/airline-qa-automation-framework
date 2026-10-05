package com.fasila.automation.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Every test class extends this one. It handles opening a browser before each
 * test and closing it after — so individual test classes don't repeat that logic.
 */
public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://blazedemo.com/";

    @BeforeMethod
    public void setUp() {
        // WebDriverManager downloads/matches the correct chromedriver version automatically
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // GitHub Actions (and most CI servers) have no physical screen attached,
        // so Chrome has to run "headless" — no visible window — or it would
        // crash immediately. GitHub Actions automatically sets a CI=true
        // environment variable on every run, so we use that to detect it:
        // locally you still see the real Chrome window as before; in CI it
        // runs invisibly in the background. No manual switching needed.
        boolean isRunningInCI = System.getenv("CI") != null;
        if (isRunningInCI) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    /** Lets the Extent Reports listener grab a screenshot on failure. */
    public WebDriver getDriver() {
        return driver;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
