package com.ttn.WebAutomation.listeners;

/**
 * This Java program demonstrates the Listener Class of PRISM-Framework.
 *
 * @author TTN
 */

import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.DetailReport;
import com.ttn.WebAutomation.utillib.GlobalVariables;
import com.ttn.WebAutomation.utillib.PropertyReader;
import com.ttn.WebAutomation.utillib.ScreenshotAs;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class MyProjectListener extends BaseLib implements ITestListener, IAlterSuiteListener {
    private static int iPassCount, iFailCount, iSkippedCount = 0;
    // private static Logger actiLog;
    private long startTime;
    private Logger logger = Logger.getLogger(MyProjectListener.class.getName());


    @Override
    public void alter(List<XmlSuite> suites) {
        int count = Integer.parseInt(System.getProperty("ThreadCount", "-1"));
        if (count <= 0) {
            return;
        }
        for (XmlSuite suite : suites) {
            suite.setThreadCount(count);
        }
    }

    @Override
    public void onStart(ITestContext arg0) {
        startTime = System.currentTimeMillis();

    }

    // ITestListener
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Start on teststart");
        ExtentTest child = getClassLevelLogger().get().createNode(result.getName());
        BaseLib.getMethodLevelLogger().set(child);

    }

    // ITestListener
    @Override
    public void onTestSuccess(ITestResult result) {

        iPassCount++;
        MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN);

        logger.info("Test Completed   ");
        logger.info("\n****************************** Ending Testcase ******************************\n");
    }


    // ITestListener
    @Override
    public synchronized void onTestFailure(ITestResult result) {

        iFailCount++;

        if (getTestLogger() != null) {

            String excepionMessage = Arrays.toString(result.getThrowable().getStackTrace());
            getTestLogger().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>"
                    + "Exception Occured:Click to see" + "</font>" + "</b >" + "</summary>"
                    + excepionMessage.replaceAll(",", "<br>") + "</details>" + " \n");
            getTestLogger().log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
        }
        String snapshotFileName = DetailReport.getReportLocation() + "/" + DetailReport.getTestId() + "/" + "fail.png";
        String extentReportSnapshotFileName = DetailReport.getTestId() + "/" + "fail.png";
        File snapshotFile = ((TakesScreenshot) BaseLib.getActiveDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(snapshotFile, new File(snapshotFileName));
        } catch (IOException e) {
            logger.log(Level.INFO, "Error occured, while creating the snapshot file" + snapshotFileName, e);
        }
        try {
            if (getTestLogger() != null) {
                getTestLogger().fail(result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
            }
        } catch (IOException e) {
            logger.info("On Test Failure getTestLogger is null");
        }
        try {

            ExtentTest extentTest = BaseLib.test;
            if (extentTest != null) {
                extentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
            }
            ScreenshotAs screenshot = new ScreenshotAs(result);
            String screenshotDestination = screenshot.getScreenshot();
            Reporter.log("(" + result.getName() + ")" + "Taking screenshot of failed test " + "("
                    + result.getTestContext().getName() + ")" + "at location - " + screenshotDestination, true);
            try {
                if (extentTest != null) {
                    extentTest.fail(result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotDestination).build());
                }

            } catch (IOException e) {
                logger.info("Extent Test report is null");
            }

            getTestLogger().fail(result.getThrowable().getClass().getSimpleName());
            getTestLogger().info("Test Completed ");

            logger.info("Test Completed");

            logger.info(
                    "\n *********************************** Ending Testcase *********************************** \n");

            // All Jira Related stuff Below -----------

            try {
                properties = PropertyReader.setup_properties();
            } catch (IOException e) {
                logger.info("Property Reader setup properties not working");
            }
            String jirastatus = properties.getProperty("jira_status");
            logger.info("The status of jira is" + jirastatus);

        } catch (TestLinkAPIException e) {
            logger.info("TestLinkAPI not working");
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        GlobalVariables.TEST_RESULT_COUNT.add("skip");
        iSkippedCount++;

        if (getTestLogger() != null) {
            getTestLogger().log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            getTestLogger().skip(result.getThrowable());
        }
        ExtentTest extentTest = BaseLib.test;
        if (extentTest != null) {
            extentTest.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            extentTest.skip(result.getThrowable());
        }
        getTestLogger().skip("Test skipped " + result.getThrowable());
        getTestLogger().info("Test Completed");

        logger.info("\n *********************************** Ending Testcase *********************************** \n");

    }
}
