package com.ttn.WebAutomation.base;
/**
 * @author TTN
 */

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.ttn.WebAutomation.utillib.GlobalVariables;
import org.apache.commons.io.FilenameUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    protected static ExtentReports extent = null;
    protected static ExtentHtmlReporter htmlReporter = null;
    protected static String filePath = null;

    public static String OS = System.getProperty("os.name");


    public synchronized static ExtentReports getReporter(String browser, String environment, String ReportName) {
        if (extent == null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy,hh_mm_ss");
            Date date = new Date();
            GlobalVariables.REPORT_PATH = BaseLib.detailReport.getReportLocation() + "/" + ReportName
                    + "-" + dateFormat.format(date) + "_" + browser + ".html";
            filePath = GlobalVariables.REPORT_PATH;
            GlobalVariables.REPORT_NAME = FilenameUtils.getName(GlobalVariables.REPORT_PATH);
            extent = new ExtentReports();
            extent.setSystemInfo("Application Name", "Demo");
            extent.setSystemInfo("User Name", "Demo");
            extent.setSystemInfo("Environment", environment);
            extent.setSystemInfo("OS", OS);
            extent.attachReporter(getHtmlReporter());
            extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
        }
        return extent;
    }

    public static ExtentHtmlReporter getHtmlReporter() {
        htmlReporter = new ExtentHtmlReporter(filePath);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Vahan Web Portal Report");
        htmlReporter.config().setReportName("Vahan Web Portal Automation Report");
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setProtocol(Protocol.HTTPS);
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setCSS(".subview-left{width: 35%;}");
        htmlReporter.config().setCSS("css-string");
        htmlReporter.config().setJS("js-string");
        htmlReporter.setAppendExisting(false);
        return htmlReporter;
    }
}