package com.e2e.integrationtests.step.definition;

import com.e2e.integrationtests.config.WebDriverConfig;
import cucumber.api.java.Before;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

@EnableAutoConfiguration
@SpringBootTest(classes = {WebDriverConfig.class})
public class BrowserStepsDefs {

    private static boolean registeredShutdownHook = false;

    @Autowired
    private WebDriver webDriver;

    @Before
    public void initialSetup() {
        driver = webDriver;
        closeAnyOpenExtraTabs();
        driver.manage().deleteAllCookies();
        if (!registeredShutdownHook) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                webDriver.quit();
                while (true) {
                    try {
                        Thread.sleep(100);
                        webDriver.close();
                    } catch (NoSuchSessionException exceptionWeAreWaitingFor) {
                        return;
                    } catch (InterruptedException shouldNotHappen) {
                        // will not happen
                    }
                }
            }));
            registeredShutdownHook = true;
        }
    }

    private void closeAnyOpenExtraTabs() {
        webDriver.getWindowHandles().stream()
                .skip(1)
                .forEach(windowHandle -> webDriver.switchTo().window(windowHandle).close());
        String handleOfLastOpenWindow = webDriver.getWindowHandles().iterator().next();
        webDriver.switchTo().window(handleOfLastOpenWindow);
    }

}
