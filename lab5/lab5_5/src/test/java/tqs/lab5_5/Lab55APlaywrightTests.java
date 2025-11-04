package tqs.lab5_5;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UsePlaywright
class Lab55APlaywrightTests {

    @Test
    void wikipediaTest(Page page) {
        page.navigate("https://www.wikipedia.org/");
        assertThat(page.title()).contains("Wikipedia");
    }

    @Test
    void bonigarciaTest(Page page) {
        page.navigate("https://bonigarcia.dev/selenium-webdriver-java/");
        assertThat(page.title()).contains("Selenium WebDriver");

        page.click("text=Slow calculator");

        assertThat(page.url()).contains("slow-calculator.html");
    }
}