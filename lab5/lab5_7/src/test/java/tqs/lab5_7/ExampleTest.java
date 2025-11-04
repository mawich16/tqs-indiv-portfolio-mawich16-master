package tqs.lab5_7;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
public class ExampleTest {
  @Test
  void test(Page page) {
    page.navigate("https://cover-bookstore.onrender.com/");
    assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"))).isVisible();

    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors,")).click();
    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors,")).fill("Harry Potter");
    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors,")).press("Enter");
    assertThat(page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Harry Potter and the Sorcerer"))).isVisible();

    page.getByText("Harry Potter and the Sorcerer").click();
  }
}