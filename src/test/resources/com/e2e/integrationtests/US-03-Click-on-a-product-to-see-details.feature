Feature: Click on a Product to a cart from the Amazon Website

  Background:
    Given I navigate to Amazon
    And I search with "Java" on the search bar

  Scenario: Click on a product to verify the details
    When I select the 1st product from the list
    Then the details of the selected product "Java" is displayed