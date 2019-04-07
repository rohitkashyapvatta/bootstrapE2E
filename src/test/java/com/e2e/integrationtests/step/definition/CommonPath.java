package com.e2e.integrationtests.step.definition;

import com.github.loyada.jdollarx.Path;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.BasicPath.span;
import static com.github.loyada.jdollarx.ElementProperties.hasAttribute;

public class CommonPath {

    public static final Path DEPARTMENT_LIST = span.withTextContaining("Departments");
    public static final Path ELECTRONIC_LINK = span.withTextContaining("Electronics");
    public static final Path HEADPHONES_LINK = span.withTextContaining("headphones");

    public static final Map<String, Path> LINKS_BY_NAME =
            ImmutableMap.<String, Path>
                    builder()
                    .put("departments", DEPARTMENT_LIST)
                    .put("electronics", ELECTRONIC_LINK)
                    .put("headphones", HEADPHONES_LINK)
                    .build();

    public static final Path ADD_TO_CART_BUTTON = element.that(hasAttribute("id", "add-to-cart-button"));
    public static final Map<String, Path> BUTTON_BY_NAME =
            ImmutableMap.<String, Path>
                    builder()
                    .put("Add to Cart", ADD_TO_CART_BUTTON)
                    .build();
}
