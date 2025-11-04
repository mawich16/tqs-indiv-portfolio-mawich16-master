Feature: Library

  Scenario: Find books by author
    Given the following books in the library
      | title                                | author      | published  |
      | The Devil in the White City          | Erik Larson | 2002-10-10 |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 2005-10-10 |
      | In the Garden of Beasts              | Erik Larson | 2009-10-10 |
    When I search for books by author 'Erik Larson'
    Then I find 2 books 

  Scenario: Find books by date
    Given the following books in the library
      | title                                | author      | published  |
      | The Devil in the White City          | Erik Larson | 2002-10-10 |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 2005-10-10 |
      | In the Garden of Beasts              | Erik Larson | 2009-10-10 |
    When I search for books by date 2000-1-1 to 2003-1-1
    Then I find 1 books

  Scenario: Zero books found by date
    Given the following books in the library
      | title                                | author      | published  |
      | The Devil in the White City          | Erik Larson | 2002-10-10 |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 2005-10-10 |
      | In the Garden of Beasts              | Erik Larson | 2009-10-10 |
    When I search for books by date 2000-1-1 to 2001-1-1
    Then I find 0 books