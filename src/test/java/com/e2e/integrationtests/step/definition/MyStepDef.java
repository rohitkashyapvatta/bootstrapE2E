package com.e2e.integrationtests.step.definition;

import com.e2e.integrationtests.config.WebConfigProps;
import com.github.loyada.jdollarx.Path;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.openqa.selenium.Keys.ENTER;

public class MyStepDef {

    @Autowired
    private WebConfigProps webConfigProps;

    @When("^I navigate to Amazon$")
    public void navigateToWebsite() {
        driver.get(webConfigProps.getBaseUrl());
    }

    @Then("the amazon home page is displayed")
    public void theAmazonHomePageIsDisplayed() {
        Path amazonNavLogo = anchor.that(hasAttribute("aria-label", "Amazon"));
        assertThat(amazonNavLogo, isPresent());
    }

    @SneakyThrows
    @When("^I search with \"([^\"]*)\" on the search bar$")
    public void iTypeOnTheSearchBar(String searchProduct) {
        Path searchBox = input.that(hasAttribute("id", "twotabsearchtextbox"));

        find(searchBox).clear();
        sendKeys(searchProduct).to(searchBox);
        sendKeys(ENTER).to(searchBox);
    }

    @Then("^the list of search results are displayed for \"([^\"]*)\"$")
    public void theListOfSearchResultsAreDisplayed(String searchedProduct) {
        Path searchResultsList = span.withTextContaining(searchedProduct).inside(div.that(hasId("search")));
        assertThat(searchResultsList, isPresent());
    }

    @When("^I click on the first matching product \"([^\"]*)\" from the displayed product results$")
    public void iClickOnTheFirstProductFromTheDisplayedProductResults(String product) {
        Path correctListOfResults = anchor.that(contains(span.withTextContaining(product)));
        clickOn(correctListOfResults);
    }

    @Then("^the details of the selected product \"([^\"]*)\" is displayed$")
    public void theDetailsOfTheSelectedProductIsDisplayed(String searchedProduct) {
        Path titleOfProduct = span
                .that(hasAttribute("id", "productTitle"))
                .and(hasTextContaining(searchedProduct));
        assertThat(titleOfProduct, isPresent());
    }
}