Feature: Book search on Cover Bookstore

  Scenario: Search for a book by title (Harry Potter)
    Given I open the Cover Bookstore homepage
    And I set the browser window size to 1205 by 843
    When I search for "Harry Potter"
    And I press Enter to search
    Then I should see a list of books
    When I click on the first book title
    Then I should see the book details page
