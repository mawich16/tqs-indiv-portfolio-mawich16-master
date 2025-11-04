package tqs.lab5_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.seljup.Options;

@ExtendWith(SeleniumJupiter.class)
class Lab51SeleniumJupiterTests {

    @Options
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    {
        // Specify binary path if needed (adjust for your OS)
        firefoxOptions.setBinary("/usr/bin/firefox");

        // Use a fresh temporary profile to avoid “profile cannot be loaded” errors
        firefoxOptions.setProfile(new FirefoxProfile());
    }

    @Test
    void bonigarciaTest(FirefoxDriver driver) {
        // Navigate to the main page
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        assertThat(driver.getTitle()).contains("Selenium WebDriver");

        WebElement slowCalculator = driver.findElement(By.linkText("Slow calculator"));
        slowCalculator.click();
        
        assertThat(driver.getCurrentUrl()).contains("slow-calculator.html");
        
    }

     @Test
    void testBlazeDemoConfirmation(FirefoxDriver driver) {
        driver.get("https://blazedemo.com/index.php");
        driver.findElement(By.name("fromPort")).sendKeys("Boston");
        driver.findElement(By.name("toPort")).sendKeys("London");
        driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
        driver.findElement(By.cssSelector("input.btn.btn-small")).click();

        String actualTitle = driver.getTitle();
        assertEquals("BlazeDemo Confirmation", actualTitle,
                "Page title should match expected confirmation page");
    }
}