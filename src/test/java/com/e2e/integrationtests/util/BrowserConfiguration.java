package com.e2e.integrationtests.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@AllArgsConstructor
@Getter
public enum BrowserConfiguration {
    DESKTOP(new Rectangle(1280, 1024));

    private final Rectangle windowSize;
}
