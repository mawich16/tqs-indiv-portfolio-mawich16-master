package tqs.lab5_6;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright
public class RefactorExampleTest {

  @Test
  void testFlightBooking(Page page) {
    page.navigate("https://blazedemo.com/");
    assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Travel The World")))
        .isVisible();

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
    assertThat(page.locator("table.table")).isVisible();

    page.locator("input[type='submit']").first().click();

    assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Your flight from")))
        .isVisible();

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Purchase Flight")).click();

    assertThat(page.locator("h1")).hasText("Thank you for your purchase today!");
  }
}
