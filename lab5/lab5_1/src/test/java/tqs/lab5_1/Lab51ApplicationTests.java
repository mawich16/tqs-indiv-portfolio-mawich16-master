package tqs.lab5_1;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

class Lab51ApplicationTests {

	WebDriver driver;

    @BeforeEach
    void setup() {
        // Setup ChromeDriver (Brave uses ChromeDriver)
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options for Brave
        ChromeOptions options = new ChromeOptions();
        
        options.setBinary("/usr/bin/brave-browser");
        
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

	@Test
    void wikipediaTest() {
		driver.get("https://www.wikipedia.org/");
		assertThat(driver.getTitle()).contains("Wikipedia");
	}

    @Test
    void bonigarciaTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        assertThat(driver.getTitle()).contains("Selenium WebDriver");
    
		WebElement slowCalculator = driver.findElement(By.linkText("Slow calculator"));
	 	slowCalculator.click();
    
    	assertThat(driver.getCurrentUrl()).contains("slow-calculator.html");
	}
}