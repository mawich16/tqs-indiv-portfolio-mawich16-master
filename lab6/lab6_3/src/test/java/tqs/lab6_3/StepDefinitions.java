package tqs.lab6_3;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class StepDefinitions {

    WebDriver driver;
    WebDriverWait wait;

    @Given("I open the Cover Bookstore homepage")
    public void i_open_the_cover_bookstore_homepage() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://cover-bookstore.onrender.com/");
    }

    @And("I set the browser window size to {int} by {int}")
    public void i_set_the_browser_window_size(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @When("I search for {string}")
    public void i_search_for(String query) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='book-search-input'], .Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")
        ));
        searchInput.click();
        searchInput.sendKeys(query);
    }

    @And("I press Enter to search")
    public void i_press_enter_to_search() {
        WebElement searchInput = driver.findElement(
            By.cssSelector("[data-testid='book-search-input'], .Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")
        );
        searchInput.sendKeys(Keys.ENTER);
    }

    @Then("I should see a list of books")
    public void i_should_see_a_list_of_books() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='book-search-item'], .SearchList_bookInfoContainer__bkMz1")
        ));
    }

    @When("I click on the first book title")
    public void i_click_on_the_first_book_title() {
        WebElement firstTitle = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".SearchList_bookTitle__1wo4a")
        ));
        firstTitle.click();
    }

    @Then("I should see the book details page")
    public void i_should_see_the_book_details_page() {
        // Wait for details to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("div[id='root']")
        ));
        driver.quit();
    }
}
