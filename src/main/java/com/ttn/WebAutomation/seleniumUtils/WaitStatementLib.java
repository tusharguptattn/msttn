package com.ttn.WebAutomation.seleniumUtils;


import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class WaitStatementLib {


    public synchronized static void implicitWaitForSeconds(WebDriver driver, int time) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
    }

}
