package com.e2e.integrationtests.step.definition;

import com.github.loyada.jdollarx.Path;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.SneakyThrows;

import static com.e2e.integrationtests.step.definition.CommonPath.BUTTON_BY_NAME;
import static com.e2e.integrationtests.step.definition.CommonPath.LINKS_BY_NAME;
import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.clickOn;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.hoverOver;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommonStepDefs {
    @When("I hover over \"([^\"]*)\" section")
    public void hoverOverSection(String section) {
        Path expectedSection = LINKS_BY_NAME.get(section);
        hoverOver(expectedSection);
    }

    @When("^I (?:click|select) (?:on|the) \"([^\"]*)\" link$")
    public void clickOnLink(String link) {
        Path expectedLink = LINKS_BY_NAME.get(link);
        clickOn(expectedLink);
    }

    @SneakyThrows
    @When("^I select the (\\d+)(?:.*)? product from the list$")
    public void selectSpecificProduct(int productRow) {
        Path rowOfExpectedElement = div.that(hasAttribute("data-index", valueOf(productRow - 1)))
                .describedBy("expected row");
        Path expectedProduct = anchor.that(hasClass("a-link-normal"))
                .inside(header5).descendantOf(div.withClass("sg-row")
                        .that(isNthSibling(productRow)).inside(rowOfExpectedElement))
                .describedBy("expected product");
        clickOn(expectedProduct);
    }

    @SneakyThrows
    @When("I click on the \"([^\"]*)\" button")
    public void clickOnButton(String buttonName) {
        Path expectedButton = BUTTON_BY_NAME.get(buttonName);
        clickOn(expectedButton);
    }

    @Then("a message \"([^\"]*)\" is displayed")
    public void informatoryMessage(String messageText) {
        Path expectedMessage = header1.or(header2).withTextContaining(messageText);
        assertThat(expectedMessage, isPresent());
    }

}
