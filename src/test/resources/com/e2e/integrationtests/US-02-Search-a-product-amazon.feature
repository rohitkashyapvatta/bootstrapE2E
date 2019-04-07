Feature: Search a Product on the Amazon Website

  Background:
    Given I navigate to Amazon
    And the amazon home page is displayed

  Scenario: Search a product and verify the product in the search list of results
    When I search with "Macbook Pro" on the search bar
    Then the list of search results are displayed for "Macbook"