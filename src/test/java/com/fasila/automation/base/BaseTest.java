package com.fasila.automation.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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

        driver = new ChromeDriver();
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
