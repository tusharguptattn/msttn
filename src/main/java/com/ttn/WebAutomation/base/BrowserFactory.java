package com.ttn.WebAutomation.base;

/**
 * This Java program demonstrates the Browser initiated code .
 *
 * @author TTN
 */

import com.google.gson.JsonObject;
import com.ttn.WebAutomation.utillib.DirectoryManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.HashMap;

public class BrowserFactory {

    public static final String HEADLESS = "--headless=new";
    private WebDriver driver;

    public synchronized WebDriver getBrowser(String browserName) {
        String lBrowserName = browserName.toLowerCase();
        switch (lBrowserName) {
            case "firefox":
                configFirefoxDriver();
                break;
            case "edge":
                configEdgeDriver();
                break;
            case "chrome":
                configChromeDriver();
                break;
            case "safari":
                configSafariDriver();
                break;
            default:
                configChromeDriver();
        }
        return driver;
    }

    /*
     * Chrome Browser Configuration Method
     */
    private void configChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(HEADLESS);
        HashMap<String, Integer> contentSettings = new HashMap<>();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        HashMap<String, Object> profile = new HashMap<>();
//        contentSettings.put("geolocation", 1);
//        contentSettings.put("notifications", 1);
        profile.put("managed_default_content_settings", contentSettings);
        chromePrefs.put("profile", profile);
        chromePrefs.put("download.default_directory", System.getProperty("user.dir")+"/Reports");
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("window-size=1366x768");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("enable-geolocation");
        try {
            driver = new ChromeDriver(options);
        } catch (Exception e) {
            Reporter.log(e.getMessage());
        }
        Reporter.log("Chrome Launched in " + BaseLib.OS, true);
    }


    /*
     * Firefox Browser Configuration Method
     */
    private void configFirefoxDriver() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments(HEADLESS);

        firefoxOptions.addArguments("-private");
        driver = WebDriverManager.firefoxdriver().capabilities(firefoxOptions).create();
        Reporter.log("Firefox Launched in " + BaseLib.OS, true);
    }

    /*
     * Internet Explorer Browser Configuration Method
     */
    private void configEdgeDriver() {
        driver = WebDriverManager.edgedriver().create();
        Reporter.log("Edge Launched in " + BaseLib.OS, true);
    }

    /*
     * Safari Browser Configuration Method
     */
    private void configSafariDriver() {
        try {
            SafariOptions options = new SafariOptions();
            options.setCapability("safari:diagnose", true);
            driver = new SafariDriver(options);
        } catch (Exception e) {
            Reporter.log(e.getMessage(), true);
            Assert.fail("Error in Safari Driver Configuration" + e.getMessage());
        }
        Reporter.log("Safari Launched in " + BaseLib.OS, true);
    }

}