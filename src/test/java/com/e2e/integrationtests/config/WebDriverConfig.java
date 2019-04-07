package com.e2e.integrationtests.config;


import com.e2e.integrationtests.util.BrowserConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.WARNING;
import static org.openqa.selenium.OutputType.FILE;
import static org.openqa.selenium.logging.LogType.BROWSER;

@Configuration
@ComponentScan("com.e2e")
public class WebDriverConfig {
    private static final String E2E_TEST_DIRECTORY_NAME = "features.e2e-test";

    @Bean
    public WebDriver webDriver(@Value("${chrome.headless}") boolean chromeHeadless,
                               @Value("${chrome.configuration}")
                                       BrowserConfiguration browserConfiguration) {
        ChromeOptions chromeOptions = new ChromeOptions();

        if (chromeHeadless) {
            chromeOptions.addArguments("headless");
        }
        chromeOptions.addArguments("--disable-gpu");
        Rectangle windowSize = browserConfiguration.getWindowSize();


        chromeOptions.addArguments(
                format("--window-size=%s,%s", windowSize.width, windowSize.height)
        );

        DesiredCapabilities cap = DesiredCapabilities.chrome();

        // Set logging preference In Google Chrome browser capability to log
        // browser errors.
        LoggingPreferences pref = new LoggingPreferences();
        pref.enable(BROWSER, WARNING);
        cap.setCapability(CapabilityType.LOGGING_PREFS, pref);
        chromeOptions.merge(cap);

        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().timeouts().implicitlyWait(5, SECONDS);

        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(chromeDriver);
        eventFiringWebDriver.register(new AbstractWebDriverEventListener() {
            @Override
            public void onException(Throwable throwable,
                                    WebDriver driver) {
                if (throwable instanceof WebDriverException) {
                    return;
                } else {
                    File chromeCapturesDirectory = getChromeCapturesDirectory();
                    File chromeCapturesPrefix = new File(chromeCapturesDirectory,
                            "Error-" + currentTimeMillis() + "-");
                    captureScreenShot(chromeDriver, chromeCapturesPrefix);
                    captureHtml(chromeDriver, chromeCapturesPrefix);
                    captureBrowserLog(chromeDriver, chromeCapturesPrefix);
                    throw (RuntimeException) throwable;
                }
            }

            @Override
            public void beforeNavigateTo(String url, WebDriver driver) {
                super.beforeNavigateTo(url, driver);
            }
        });

        return eventFiringWebDriver;
    }

    private File getChromeCapturesDirectory() {
        File currentDirectory = new File(getProperty("user.dir"));
        File e2eTestDirectory;
        if (currentDirectory.toPath().endsWith(E2E_TEST_DIRECTORY_NAME)) {
            e2eTestDirectory = currentDirectory;
        } else {
            e2eTestDirectory = new File(currentDirectory, E2E_TEST_DIRECTORY_NAME);
        }
        File chromeCapturesDirectory = Paths.get(e2eTestDirectory.toString(), "target",
                "e2eChromeCaptures").toFile();

        if (!chromeCapturesDirectory.exists() && !chromeCapturesDirectory.mkdirs()) {
            throw new RuntimeException("failed to create directory even though it wasn't present");
        }

        return chromeCapturesDirectory;
    }

    private static void captureScreenShot(ChromeDriver driver, File prefix) {
        final Path targetFile = new File(prefix + "screenshot.png").toPath();

        File file = driver.getScreenshotAs(FILE);
        try {
            copy(file.toPath(), targetFile, REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to copy screenshot to " + targetFile, ex);
        }
    }

    private static void captureHtml(ChromeDriver driver, File prefix) {
        final Path targetFile = new File(prefix + "page.html").toPath();


        String html = driver.findElement(By.xpath("/html/body")).getAttribute("innerHTML");

        try (
                FileWriter fw = new FileWriter(targetFile.toFile())
        ) {
            fw.write(html);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to copy html to " + targetFile, ex);
        }
    }

    private static void captureBrowserLog(ChromeDriver driver, File prefix) {
        final Path targetFile = new File(prefix + "browser-log.txt").toPath();


        final List<LogEntry> browserLogEntries = driver.manage().logs().get("browser").getAll();
        try (
                FileWriter fw = new FileWriter(targetFile.toFile());
                PrintWriter pw = new PrintWriter((fw))
        ) {
            browserLogEntries.stream().map(Object::toString).forEach(pw::println);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to copy browser log to " + targetFile, ex);
        }
    }
}
