package tqs.lab5_6;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
class ExampleTest {
  @Test
  void test(Page page) {
    page.navigate("https://blazedemo.com/");
    assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Travel The World"))).isVisible();

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
    assertThat(page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin"))).isVisible();

    page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin")).getByRole(AriaRole.BUTTON).click();
    assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Travel The World"))).isVisible();

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Purchase Flight")).click();
  }
}