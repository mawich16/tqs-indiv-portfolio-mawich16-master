package tqs.lab5_5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.nio.file.Paths;

class Lab55ApplicationTests {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        
        // Launch Brave browser (Chromium-based)
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(1000)
                .setExecutablePath(Paths.get("/usr/bin/brave-browser"))  // Path to Brave
        );
        
        page = browser.newPage();
    }

    @AfterEach
    void teardown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void wikipediaTestWithBrave() {
        page.navigate("https://www.wikipedia.org/");
        assertThat(page.title()).contains("Wikipedia");
    }

    @Test
    void bonigarciaTest() {
        page.navigate("https://bonigarcia.dev/selenium-webdriver-java/");
        assertThat(page.title()).contains("Selenium WebDriver");

        page.click("text=Slow calculator");

        assertThat(page.url()).contains("slow-calculator.html");
    }
}
