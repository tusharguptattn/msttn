package com.ttn.WebAutomation.seleniumUtils;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.utillib.GetPropertyValues;
import com.ttn.WebAutomation.utillib.GlobalVariables;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * this class creates the test detail report
 *
 * @author TTN
 */

public class DetailReport {
    GlobalVariables globalVariables = new GlobalVariables();

    /**
     * add test step with given detail
     *
     * @param stepName
     * @param description
     * @param status
     */
    public synchronized void addStep(String stepName, String description,
                                     Status status) {
        addStep(stepName, description, null, null, status);
    }

    /**
     * add test step report with given information
     *
     * @param stepName
     * @param description
     * @param expectedResult
     * @param actualResult
     * @param status
     */
    public synchronized void addStep(String stepName, String description,
                                     String expectedResult, String actualResult, Status status) {
        expectedResult = (StringUtils.isEmpty(expectedResult))
                ? "" : expectedResult;
        actualResult = (StringUtils.isEmpty(actualResult))
                ? "" : actualResult;

        try {

            // take the screenshot if applicable
            if (((BaseLib.getActiveDriver() != null)
                    && (GetPropertyValues.getGenericProperty("snapshotLevel").equalsIgnoreCase("all")
                    || GetPropertyValues.getGenericProperty("snapshotLevel").equalsIgnoreCase(status.toString())))) {
                String extentReportSnapshotFileName = testId + "/" + stepCounter + ".png";

                if (status.toString().equalsIgnoreCase("fail")) {
                    if (!expectedResult.equals("") && !actualResult.equals("")) {
                        BaseLib.getTestLogger().fail("<b>" + stepName + "</b>" + "<BR>" + description + "<BR>" + "actualResult=" + actualResult
                                        + "<BR>" + "expectedResult=" + expectedResult,
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
                    } else {
                        BaseLib.getTestLogger().fail("<b>" + stepName + "</b>" + "<BR>" + description + "<BR>",
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
                    }
                } else if (status.toString().equalsIgnoreCase("pass")) {
                    if (!expectedResult.equals("") && !actualResult.equals("")) {
                        BaseLib.getTestLogger().pass("<b>" + stepName + "</b>" + "<BR>" + description + "<BR>" + "actualResult=" + actualResult
                                + "<BR>" + "expectedResult=" + expectedResult);
//                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
                    } else {
                        BaseLib.getTestLogger().pass("<b>" + stepName + "</b>" + "<BR>" + description + "<BR>");
//                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
                    }
                }
                stepCounter++;
            } else {
                if (!expectedResult.equals("") && !actualResult.equals("")) {
                    BaseLib.getTestLogger().log(status, "<b>" + stepName + "</b>" + "<BR>" + description + "<BR>" + "actualResult=" + actualResult
                            + "<BR>" + "expectedResult=" + expectedResult);
                } else {
                    BaseLib.getTestLogger().log(status, "<b>" + stepName + "</b>" + "<BR>" + description + "<BR>");
                }
            }


        } catch (Exception e) {
            logger.log(Level.INFO, "Error occured, while adding step: "
                    + stepName + "(" + description + ")", e);
        }
//        stepCounter++;
    }

    /**
     * set the test case id
     *
     * @param testId
     */
    public synchronized void setTestId(String testId) {
        this.testId = testId;
    }

    /**
     * get the test case id
     *
     * @return
     */
    public synchronized static String getTestId() {
        return testId;
    }

    /**
     * gets the count of failed test steps
     *
     * @return
     */
    public int getNumberOfFailedSteps() {
        return numberOfFailedSteps;
    }

    /**
     * gets the test report folder name
     * from the global.properties file
     *
     * @return
     * @throws FrameworkException
     */
    public synchronized static String getReportLocation() throws FrameworkException {
        return reportLocation;
    }

    /**
     * set selenium-helper instance
     *
     * @param seleniumHelper
     */
    public void setSeleniumHelper(SeleniumHelper seleniumHelper) {
        this.seleniumHelper = seleniumHelper;
    }


    public synchronized void setReportLocation(String uniqueFolderName) {
       /* reportLocation = GlobalConfiguration.getDynamicProperty(
                "reportFolder") + "/" + uniqueFolderName;*/
        reportLocation = System.getProperty("user.dir") + "/Automation Reports" + "/" + uniqueFolderName;
    }


    /**
     * parameterized constructor to create test
     * detail report with unique folder name
     *
     * @param uniqueFolderName
     * @throws FrameworkException
     */
    public DetailReport(String uniqueFolderName)
            throws FrameworkException {
        File automationReportsFolder = new File(System.getProperty("user.dir") + "/Automation Reports");
        File OutputFolder = new File(System.getProperty("user.dir") + "/Automation Reports" + "/" + uniqueFolderName);
        createDirectory(automationReportsFolder);
        createDirectory(OutputFolder);
        reportLocation = System.getProperty("user.dir") + "/Automation Reports" + "/" + uniqueFolderName;
    }

    private synchronized void createDirectory(File directory) {
        if (!directory.exists()) {
            if (directory.mkdir()) {
                logger.log(Level.INFO, directory + " =>Directory is created!");
            } else {
                logger.log(Level.INFO, directory + " =>Failed to create Directory");
                throw new FrameworkException();
            }
        } else {
            logger.log(Level.INFO, directory + " =>Directory already exists");
        }
    }

    public DetailReport() {
    }

    private Logger logger = Logger.getLogger(DetailReport.class.getName());

    private int numberOfFailedSteps;

    private static String reportLocation;

    private SeleniumHelper seleniumHelper;

    private int stepCounter = 1;

    public synchronized int getStepCounter() {
        return stepCounter;
    }

    public synchronized void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    private static String testId;

}