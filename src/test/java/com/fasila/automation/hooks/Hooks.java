package com.fasila.automation.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

/**
 * The Cucumber equivalent of BaseTest.java.
 *
 * Why a separate class instead of reusing BaseTest? BaseTest's setUp/tearDown
 * use TestNG's @BeforeMethod/@AfterMethod annotations — those only fire when
 * TestNG runs a @Test method directly. Cucumber scenarios are executed by
 * Cucumber's own engine (even though cucumber-testng plugs it into TestNG at
 * the top level), so we need Cucumber's own @Before/@After hooks instead.
 *
 * `driver` is static so step definition classes (like LoginSteps) can reach
 * it via Hooks.driver without needing a dependency-injection framework —
 * simplest option while you're learning. A more advanced framework would use
 * PicoContainer or a shared "TestContext" object instead of a static field.
 */
public class Hooks {

    public static WebDriver driver;
    private static final String BASE_URL = "https://blazedemo.com/";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
