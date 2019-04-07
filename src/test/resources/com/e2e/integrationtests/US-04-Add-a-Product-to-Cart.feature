Feature: Add a cart from the Amazon Website

  Background:
    Given I navigate to Amazon

  Scenario: Select a product from the Parent Department and add it to the cart
    When I hover over "departments" section
    And I select the "electronics" link
    And I click on "headphones" link
    And I select the 1st product from the list
    And I click on the "Add to Cart" button
    Then a message "Added to Cart" is displayed