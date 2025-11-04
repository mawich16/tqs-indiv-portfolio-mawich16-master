package tqs.lab6_2;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class StepDefinitions {

    static final Logger log = getLogger(lookup().lookupClass());

    private Library lib;
    private List<Book> result;

    @ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
    public LocalDate iso8601Date(String year, String month, String day){
        return Utils.localDateFromDateParts(year, month, day);
    }

    @DataTableType
    public Book bookEntry(Map<String, String> tableEntry){
        return new Book(
                tableEntry.get("title"),
                tableEntry.get("author"),
                Utils.isoTextToLocalDate(tableEntry.get("published")) );
    }

    @Given("the following books in the library")
    public void setup(DataTable table) {
        lib = new Library();

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    
        for (Map<String, String> columns : rows) {
            lib.addBook(bookEntry(columns));
        }
    }

    @When("I search for books by author {string}")
    public void find_by_author(String auth) {
        log.debug("Searching for {} books", auth);

        result = lib.findBooksByAuthor(auth);
    }

    @When("I search for books by date {word} to {word}")
    public void find_by_date(String from, String to) {
        log.debug("Searching from {} to {} books", from, to);

        result = lib.findBooks(Utils.isoTextToLocalDate(from), Utils.isoTextToLocalDate(to));
    }

    @Then("I find {int} books")
    public void theScenarioPasses(Integer expected) {
        log.debug("Result: {} (expected {})", result.size(), expected);
        assertEquals(expected, result.size());
    }

}
