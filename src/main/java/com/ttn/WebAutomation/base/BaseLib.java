package com.ttn.WebAutomation.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.ttn.WebAutomation.pageObjects.vahanDashboard.RtoVahaanReports;
import com.ttn.WebAutomation.seleniumUtils.*;
import com.ttn.WebAutomation.utillib.*;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Properties;

public abstract class BaseLib {
    private String isEmailSend = "";
    protected static ThreadLocal<ExtentTest> testLevelLogger = new ThreadLocal<>();
    protected static ThreadLocal<ExtentTest> classLevelLogger = new ThreadLocal<>();
    protected static ThreadLocal<ExtentTest> methodLevelLogger = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> activeDriver = new ThreadLocal<>();
    public static ThreadLocal<RtoVahaanReports> rtoVR = new ThreadLocal<>();
    public static ThreadLocal<SeleniumHelper> seleniumHelper = new ThreadLocal<>();
    protected static Logger logger = LoggerFactory.getLogger(BaseLib.class);
    public static ExtentReports extent;
    public static ExtentTest test;
    public static Logger log;
    public static Properties properties;
    public static String OS = System.getProperty("os.name");
    public static String URL;
    public static String packageName;
    public static String browserName;


    @FindBy(xpath = "//input[@placeholder='Username']")
    private WebElement userName;

    public static RtoVahaanReports getRtoVahanReportsPage(SeleniumHelper helper, WebDriver driver3) {
        // If the LoginPage is not yet initialized, create a new instance for this thread

        if (rtoVR.get() == null) {
            rtoVR.set(new RtoVahaanReports(helper, driver3));
        }
        return rtoVR.get();
    }

    public void removeRtoVahanReportsPage() {
        // To clean up the thread-local reference if necessary
        rtoVR.remove();
    }


    public static SeleniumHelper getHelperPage(WebDriver driver3) {
        // If the LoginPage is not yet initialized, create a new instance for this thread

        if (seleniumHelper.get() == null) {
            seleniumHelper.set(new SeleniumHelper(driver3));
        }
        return seleniumHelper.get();
    }

    public void removeseleniumHelper() {
        // To clean up the thread-local reference if necessary
        seleniumHelper.remove();
    }

    public synchronized static WebDriver getActiveDriver() {
        return activeDriver.get();
    }


    public synchronized static void setActiveDriver(WebDriver driver1) {

        activeDriver.set(driver1);

    }


    public static ThreadLocal<ExtentTest> getTestLevelLogger() {
        return testLevelLogger;
    }


    public static ThreadLocal<ExtentTest> getClassLevelLogger() {
        return classLevelLogger;
    }


    public static ThreadLocal<ExtentTest> getMethodLevelLogger() {
        return methodLevelLogger;
    }

    public static void setMethodLevelLogger(ThreadLocal<ExtentTest> methodLevelLogger) {
        BaseLib.methodLevelLogger = methodLevelLogger;
    }

    public static String globalEnvironment;
    public static String testingType;

    @Parameters({"Environment", "browser", "EmailSend"})
    @BeforeSuite(groups = {"sanity", "regression"})
    public void beforeSuite(String environment, String browser, String emailSend, ITestContext context)
            throws FrameworkException, IOException {
        isEmailSend = emailSend;
        InetAddress inetAddress = InetAddress.getLocalHost();

        // Print the hostname (PC name)
        String pcName = inetAddress.getHostName();
        logger.info("PC Name: {}", pcName);
        try {
            PropertyConfigurator.configure(GlobalVariables.getPropertiesPath() + GlobalVariables.LOG4J_FILE_NAME);
        } catch (IOException e1) {

            logger.info("Exception caught {}", e1.getMessage());
        }
        logger.info(">>>>>>>>>> Starts of " + "BeforeSuite" + " <<<<<<<<<<");
        GlobalVariables.RESULT_BASE_LOCATION = Utility.generateUniqueString();
        try {
            detailReport = new DetailReport(GlobalVariables.RESULT_BASE_LOCATION);
            ThreadManager.setDetailReport(detailReport);
            extent = ExtentManager.getReporter(browser, environment, context.getCurrentXmlTest().getSuite().getName());

            properties = PropertyReader.setup_properties();
            String jirastatus = properties.getProperty("jira_status");
            logger.info("The status of jira is " + jirastatus);
        } catch (Exception e) {
            logger.info("------Error------" + e.getMessage());
        }
        logger.info("Inside Before Suite");
    }

    public static ExtentTest parentTest = null;

    @Parameters({"packageName", "Environment", "browser", "testingType"})
    @BeforeTest(groups = {"sanity", "regression"})
    public void preTestCondition(String packageName, String environment, String browser, String testtype,
                                 ITestContext testContext) {
        logger.info(">>>>>>>>>> Starts of " + "BeforeTest" + " <<<<<<<<<<");
        globalEnvironment = environment;
        BaseLib.packageName = packageName;
        BaseLib.testingType = testtype;
        BaseLib.browserName = browser;
        new GetPropertyValues();
        URL = GetPropertyValues.getEnvironmentProperty("testURL");
        parentTest = extent.createTest(packageName);
        getTestLevelLogger().set(parentTest);
        logger.info(">>>>>>>>>> Ends of " + "BeforeTest" + " <<<<<<<<<<");
    }

    public static ExtentTest testLevel = null;

    @BeforeClass(alwaysRun = true, groups = {"sanity", "regression"})
    public void beforeClass() throws Exception {
        logger.info(">>>>>>>>>> Start of " + "BeforeClass" + " <<<<<<<<<<");
        String className = getClass().getSimpleName();
        testLevel = parentTest
                .createNode("<b>" + "<font color=" + "blue>" + className + "</font>" + "</b >");

        getClassLevelLogger().set(testLevel);
        logger.info(">>>>>>>>>> Ends of " + "BeforeClass" + " <<<<<<<<<<");
    }

    @AfterClass(alwaysRun = true, groups = {"sanity", "regression"})
    public void afterClass() throws Exception {
        logger.info(">>>>>>>>>> Start of " + "AfterClass" + " <<<<<<<<<<");

    }

    @Parameters({"browser"})
    @BeforeMethod(groups = {"sanity", "regression"})
    public void preReportSetUp(String browser, Method method, ITestContext testContext) {

        boolean healthCheck = true;
        healthCheck = HealthCheckUtils.checkUrlHealth(URL);
        Assert.assertTrue(healthCheck, "[SYSTEM HEALTH CHECK] : Nexa Revamp WEB URL- " + URL + " IS Down.");
        logger.info("[SYSTEM HEALTH CHECK] :MSIL Live Dashboard IS UP and Running.");

        browser = browser.toUpperCase();
        try {
            setActiveDriver(new BrowserFactory().getBrowser(browser));
            WaitStatementLib.implicitWaitForSeconds(getActiveDriver(), 30);
            getActiveDriver().manage().window().maximize();
            getActiveDriver().navigate().to(URL);
        } catch (Exception e) {
            logger.info("Exception caught {}", e.getMessage());
        }
        Reporter.log("(" + testContext.getName() + ") " + "Navigating to URL in " + browser + " : " + URL, true);
        WaitStatementLib.implicitWaitForSeconds(getActiveDriver(), 30);
        softAssert = new SoftAssert();
        ThreadManager.setSoftAssert(softAssert);
        detailReport = new DetailReport(GlobalVariables.RESULT_BASE_LOCATION);
        ThreadManager.setDetailReport(detailReport);
        detailReport.setTestId("TC_" + method.getName().split("_")[0]);
        detailReport.setStepCounter(1);

        ThreadManager.setPreviousElement(userName);

    }


    @AfterMethod(groups = {"sanity", "regression"})
    public void reportClosure(ITestResult result, Method method) {
        System.out.println(Thread.currentThread().getName());
        removeRtoVahanReportsPage();
        removeseleniumHelper();
        DirectoryManager.cleanup();
        logger.info(">>>>>>>>>> Start of @AfterMethod <<<<<<<<<<");
        if (result.getStatus() == ITestResult.FAILURE) {
            String scrBase64 = ((TakesScreenshot) BaseLib.getActiveDriver()).getScreenshotAs(OutputType.BASE64);
            // convert the BASE64 to File type
            File file = OutputType.FILE.convertFromBase64Png(scrBase64);

            SendScreenshot.log(file, "Failed Screenshot");

            getTestLogger().log(Status.FAIL,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            getTestLogger().log(Status.FAIL,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
            GlobalVariables.TEST_RESULT_COUNT.add("fail");
        } else if (result.getStatus() == ITestResult.SKIP) {
            getTestLogger().log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
            GlobalVariables.TEST_RESULT_COUNT.add("skip");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            getTestLogger().log(Status.PASS,
                    MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
            GlobalVariables.TEST_RESULT_COUNT.add("pass");
        }
        if (Objects.nonNull(getActiveDriver())) {
            try {
                getActiveDriver().quit();
                setActiveDriver(null);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }

        extent.flush();
        logger.info(">>>>>>>>>> Ends of {}", method.getName() + " <<<<<<<<<<");

    }

    @AfterTest(groups = {"sanity", "regression"})
    public void getResult(ITestContext testContext) {
        logger.info(">>>>>>>>>> Start of @AfterTest <<<<<<<<<<");
        extent.flush();
        logger.info(">>>>>>>>>> End of @AfterTest <<<<<<<<<<");
    }

    @AfterSuite(groups = {"sanity", "regression"})
    public void afterSuite() throws IOException, FrameworkException, InterruptedException {
        logger.info(">>>>>>>>>> Start of @AfterSuite <<<<<<<<<<");
        ExtentManager.getReporter("", "", "").flush();
        try {
            if (isEmailSend.equalsIgnoreCase("Yes")) {
                Utility.sendMailWithReport();
            }
        } catch (Exception e) {
            logger.info("Error in afterSuite"+ e);
        }
        logger.info(">>>>>>>>>> Start S3 <<<<<<<<<<");
        logger.info(">>>>>>>>>> {} ", DetailReport.getReportLocation() + " <<<<<<<<<<");

    }

    public synchronized static ExtentTest getTestLogger() {
        return getMethodLevelLogger().get();
    }

    public static DetailReport detailReport;
    protected SoftAssert softAssert;

}
