package com.ttn.WebAutomation.utillib;

import com.google.common.io.BaseEncoding;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Useful for binary data (e.g. sending files to ReportPortal)
 *
 * @author Vaibhav Shukla
 */
public class SendScreenshot {

    private static final Logger LOGGER = LoggerFactory.getLogger("binary_data_logger");

    public static void log(File file, String message) {
        LOGGER.info("RP_MESSAGE#FILE#{}#{}", file.getAbsolutePath(), message);
    }

    public static void log(byte[] bytes, String message) {
        LOGGER.info("RP_MESSAGE#BASE64#{}#{}", BaseEncoding.base64().encode(bytes), message);
    }

    public static void logBase64(String base64, String message) {
        LOGGER.info("RP_MESSAGE#BASE64#{}#{}", base64, message);
    }


    public String SendScreenshot(WebElement webElement) {
        //statics only

        DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_hh_mm_ss");
        Date date = new Date();
        File srcFile = webElement.getScreenshotAs(OutputType.FILE);

        String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + dateFormat.format(date) + "_" + ".png";
        File destFile = new File(screenshotPath);

        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            Reporter.log("Not able to take the screenshot.", true);
            LOGGER.error("Error: " + e);
        }
        return screenshotPath;
    }
}

