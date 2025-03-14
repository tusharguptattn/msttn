package com.ttn.WebAutomation.seleniumUtils;

/**
 * @author TTN
 */

import com.aventstack.extentreports.Status;
import com.ttn.WebAutomation.base.BaseLib;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;

public class SeleniumHelper {

    private WebDriver driver;
    protected static Logger logger = LoggerFactory.getLogger(SeleniumHelper.class);

    /**
     * default constructor
     */
    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    /**
     * clicks on specified field (button,
     * link, menu etc.), identified by
     * given locator with follow-up
     * validation
     *
     * @param fieldName
     * @param @webElement element
     * @throws Exception
     */
    public void click(String fieldName, WebElement element) throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        try {
            logger.info("Attempting to click on the field: {}", fieldName);
            getElement(element);
            explicitWaitForElementGetVisible(fieldName, element, "12");
            element.click();
            logger.info("Successfully clicked on the field: {}", fieldName);
            logger.info("Successfully clicked on the field: {}", fieldName);
//            ThreadManager.getDetailReport().addStep(
//                    "User Clicked Action",
//                    "User Successfully clicked on the field: '" + fieldName + "'.",
//                    Status.PASS
//            );

        } catch (Throwable t) {

            logger.error("Error while clicking on the field: {}. Exception: {}", fieldName, t.getMessage());
//            ThreadManager.getDetailReport().addStep(
//                    "User Click Action",
//                    "User Failed to click on the field: '" + fieldName + "'.<br>Error: " + t.getClass().getName() + "<br>Message: " + t.getMessage(),
//                    Status.FAIL
//            );
            Assert.fail("Error clicking on the field: '" + fieldName + "'. Exception: " + t.getMessage());

        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }


    /**
     * wait for specified field got visible, located by given locator
     *
     * @param fieldName
     * @param element
     * @param waitSecondsString
     */
    public void explicitWaitForElementGetVisible(String fieldName, WebElement element, String waitSecondsString) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        int waitSeconds = 15;
        try {
            waitSeconds = Integer.parseInt(waitSecondsString);
        } catch (Exception ignored) {

        }
        try {
            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
            webDriverWait.until(ExpectedConditions.visibilityOf(element));
            logger.info("explicitWaitForElementGetVisible: The element \"" + fieldName +
                    "\" became visible within " + waitSeconds + " second(s).");

//            ThreadManager.getDetailReport().addStep(
//                    "Wait for Element to Become Visible",
//                    "User wait for '" + fieldName + "' became visible within " + waitSeconds + " second(s),",
//                    Status.PASS
//            );

        } catch (Exception t) {
            logger.error("explicitWaitForElementGetVisible: The element \"" + fieldName +
                    "\" did not become visible within " + waitSeconds + " second(s). Error: " + t.getMessage());

//            ThreadManager.getDetailReport().addStep(
//                    "Wait for Element to Become Visible",
//                    " The element '" + fieldName + "' failed to become visible within the expected " + waitSeconds + " second(s). This indicates a potential issue with page loading, the element not being rendered, or an incorrect locator. Error details: " + t.getMessage() + ".",
//                    Status.FAIL
//            );
            Assert.fail("Error while waiting for the element \"" + fieldName + "\" to become visible.");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    /**
     * check if element exists for the
     * given locator
     *
     * @param @locatorType
     * @param @locatorValue
     * @return
     * @throws Exception
     * @throws FrameworkException
     */
    public boolean isElementExists(WebElement element, String fieldName) throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        boolean isElementExists = true;
        if (driver.findElements(getBy(element)).size()
                == 0) {
            isElementExists = false;
            String elementDescription = "User verified '" + fieldName + "' does not exist on the page.";
//            ThreadManager.getDetailReport().addStep("Verify Element Exists", fieldName, Status.FAIL);
            Assert.fail(elementDescription);
        } else {
            try {
                getElement(element);
//                ThreadManager.getDetailReport().addStep(
//                        "Verify Element Exists",
//                        " User verified '" + fieldName + "' exists and is visible on the page as expected.",
//                        Status.PASS
//                );

            } catch (Throwable t) {
                // dumping exception (if any)
            }

        }
        int globalSyncSeconds = Integer.parseInt("15");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(globalSyncSeconds));
        return isElementExists;
    }

    /**
     * Put focus on specified field name
     * identified by given locator type/value
     *
     * @param fieldName
     * @param @locatorType
     * @param @locatorValue
     */
    public void focusMoveToElement(String fieldName, WebElement element) {
        try {
            explicitWaitForElementGetVisible(fieldName, element, "15");
            Actions action = new Actions(driver);
            getElement(element);
            action.moveToElement(element).build().perform();


            logger.info("focusMoveToElement: User Successfully moved focus to field: {} ", fieldName);
//            ThreadManager.getDetailReport().addStep("focusMoveToElement",
//                    "User Successfully moved focus to the field: '" + fieldName + "' on the page.",
//                    Status.PASS);
        } catch (Throwable t) {
            logger.error("focusMoveToElement: Error while moving focus to field: " + fieldName, t);
//            ThreadManager.getDetailReport().addStep("focusMoveToElement",
//                    "Error while attempting to move focus to the field: '" + fieldName + "'." +
//                            " Error Details: <BR>" + t.getMessage(), Status.FAIL);
            Assert.fail("Error while moving focus to the element: " + fieldName + ". Error: " + t.getMessage());

        }
    }


    /**
     * Put focus on specified field name
     * identified by given locator type/value
     *
     * @param fieldName
     * @param locatorType
     * @param locatorValue
     */
    public void focusMoveToElement(String fieldName, String locatorType,
                                   String locatorValue) {
        try {
            Actions action = new Actions(driver);
            WebElement element = getElement(getBy(locatorType, locatorValue));
            action.moveToElement(element).build().perform();
//            ThreadManager.getDetailReport().addStep("focusMoveToElement",
//                    "Successfully moved focus to the field: '" + fieldName + "' using locator: "
//                            + locatorType + "='" + locatorValue + "'.", Status.PASS);
        } catch (Throwable t) {
//            ThreadManager.getDetailReport().addStep("focusMoveToElement",
//                    "Error while attempting to move focus to field: '" + fieldName + "'." +
//                            " Locator: " + locatorType + "='" + locatorValue + "'. Error Details: <BR>" + t.getMessage(),
//                    Status.FAIL);
            // Fail the test with detailed assertion message
            Assert.fail("Error while moving focus to field: '" + fieldName + "' using locator: "
                    + locatorType + "='" + locatorValue + "'. Error: " + t.getMessage());
        }

    }


    /**
     * get By instance based on given locator type
     * and locator value
     *
     * @param locatorType
     * @param locatorValue
     * @return
     * @throws FrameworkException
     */
    public By getBy(String locatorType, String locatorValue)
            throws FrameworkException {

        By by = null;
        switch (locatorType.toLowerCase()) {
            case "id":
                by = By.id(locatorValue);
                break;
            case "name":
                by = By.name(locatorValue);
                break;
            case "xpath":
                by = By.xpath(locatorValue);
                break;
            case "cssselector":
                by = By.cssSelector(locatorValue);
                break;
            case "classname":
                by = By.className(locatorValue);
                break;
            case "linktext":
                by = By.linkText(locatorValue);
                break;
            case "partiallinktext":
                by = By.partialLinkText(locatorValue);
                break;
            default:
                throw new FrameworkException(
                        "Invalid locator type defined: " + locatorType);
        }
        return by;
    }

    /**
     * get By instance based on given locator type
     * and locator value
     *
     * @param @locatorType
     * @param @locatorValue
     * @return
     * @throws FrameworkException
     */
    public By getBy(WebElement element)
            throws Exception {
        String locatorTypeAndlocatorValue = element.toString();
        String locatorType = "";
        String locatorValue = "";
        int stringLength = 0;

        if (locatorTypeAndlocatorValue.contains("By.")) {
            locatorType = locatorTypeAndlocatorValue.split("By.")[1].split(":")[0].trim();
            stringLength = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().length();
            locatorValue = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().substring(0, stringLength - 1);

        } else if (locatorTypeAndlocatorValue.contains("->")) {
            locatorType = locatorTypeAndlocatorValue.split("->")[1].split(":")[0].trim();
            stringLength = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().length();
            locatorValue = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().substring(0, stringLength - 1);
        }
        By by = null;
        switch (locatorType.toLowerCase()) {
            case "id":
                by = By.id(locatorValue);
                break;
            case "name":
                by = By.name(locatorValue);
                break;
            case "xpath":
                by = By.xpath(locatorValue);
                break;
            case "css":
                by = By.cssSelector(locatorValue);
                break;
            case "classname":
                by = By.className(locatorValue);
                break;
            case "linktext":
                by = By.linkText(locatorValue);
                break;
            case "partiallinktext":
                by = By.partialLinkText(locatorValue);
                break;
            default:
                throw new Exception(
                        "Invalid locator type defined: " + locatorType);
        }
        return by;
    }

    /**
     * gets the value of text field, text-area
     * or label/button text
     *
     * @param @locatorType
     * @param @locatorValue
     * @return
     * @throws FrameworkException
     * @throws Exception
     */
    public String getData(WebElement element)
            throws Exception {
        return getData(element, true);
    }

    /**
     * gets the value of text field, text-area
     * or label/button text
     *
     * @param @locatorType
     * @param @locatorValue
     * @return
     * @throws FrameworkException
     * @throws Exception
     */
    public String getData(WebElement element, boolean hilighted)
            throws Exception {
        String actualData = "";
        if (hilighted) {
            getElement(element, true, false);
        }
        String elementTypeAttribute = element.getAttribute("type");
        if (elementTypeAttribute != null &&
                (elementTypeAttribute.equalsIgnoreCase("text")
                        || elementTypeAttribute.equalsIgnoreCase("textarea")
                        || elementTypeAttribute.equalsIgnoreCase("submit")
                        || elementTypeAttribute.equalsIgnoreCase("button"))) {
            actualData = element.getAttribute("value");
        } else {
            actualData = element.getText();
        }
        return actualData;
    }

    /**
     * gets the value of text field, text-area
     * or label/button text
     *
     * @param locatorType
     * @param locatorValue
     * @return
     * @throws FrameworkException
     * @throws Exception
     */
    public String getData(String locatorType, String locatorValue)
            throws FrameworkException, Exception {
        String actualData = "";
        WebElement element = getElement(getBy(locatorType, locatorValue), true, false);
        String elementTypeAttribute = element.getAttribute("type");
        if (elementTypeAttribute != null &&
                (elementTypeAttribute.equalsIgnoreCase("text")
                        || elementTypeAttribute.equalsIgnoreCase("textarea")
                        || elementTypeAttribute.equalsIgnoreCase("submit")
                        || elementTypeAttribute.equalsIgnoreCase("button"))) {
            actualData = element.getAttribute("value");
        } else {
            actualData = element.getText();
        }
        return actualData;
    }

    /**
     * get instance of web element based
     * on given by reference and make-sure
     * that element is displayed and in
     * enabled state
     * it also highlight the element
     * during test run
     *
     * @param @by
     * @return
     * @throws Exception
     */
    public WebElement getElement(WebElement element) throws Exception {
        return getElement(element, true, true);
    }

    /**
     * get instance of web element based
     * on given by reference, display and
     * enable check
     *
     * @param @by
     * @param isDisplayCheck
     * @param isEnabledCheck
     * @return
     * @throws Exception
     */
    public WebElement getElement(WebElement element, boolean isDisplayCheck,
                                 boolean isEnabledCheck) throws Exception {

        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='4px solid red';", element);
        }
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='none';",
                element);
        if (BaseLib.browserName.equalsIgnoreCase("chrome")) {
            Point elementLocation = element.getLocation();
            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollTo(" + "0," + (elementLocation.getY() - 150) + ");");
        }

        return element;
    }

    /**
     * get instance of web element based
     * on given by reference and make-sure
     * that element is displayed and in
     * enabled state
     * it also highlight the element
     * during test run
     *
     * @param by
     * @return
     * @throws Exception
     */
    public WebElement getElement(By by) throws Exception {
        return getElement(by, true, true);
    }

    /**
     * get instance of web element based
     * on given by reference, display and
     * enable check
     *
     * @param by
     * @param isDisplayCheck
     * @param isEnabledCheck
     * @return
     * @throws Exception
     */
    public WebElement getElement(By by, boolean isDisplayCheck,
                                 boolean isEnabledCheck) throws Exception {
        WebElement element = driver.findElement(by);
        try {
            if (ThreadManager.getPreviousElement() != null && driver
                    instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.border='none';", ThreadManager.getPreviousElement());
            }
        } catch (Throwable t) {
            //dumping exception
        }
        if (element != null) {
            // ignoring exceptions in case of IEDriver as its false alarm
            try {
                if (isDisplayCheck && (!element.isDisplayed())) {
                    throw new ElementNotInteractableException("Element is not displayed");
                }
            } catch (InvalidArgumentException e) {
                if (!(driver instanceof InternetExplorerDriver)) {
                    throw e;
                }
                logger.info("InternetExplorerDriver: Dumping InvalidArgumentException for " + element);
            } catch (JsonException e) {
                if (!(driver instanceof InternetExplorerDriver)) {
                    throw e;
                }
                logger.info("InternetExplorerDriver: Dumping JsonException for " + element);
            } catch (JavascriptException e) {
                if (!(driver instanceof InternetExplorerDriver)) {
                    throw e;
                }
                logger.info("InternetExplorerDriver: Dumping JavascriptException for " + element);
            }

            if (isEnabledCheck && (!element.isEnabled())) {
                throw new ElementNotInteractableException("Element is not enabled");
            }
            if (driver instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.border='4px solid green';",
                        //                        "arguments[0].setAttribute('style', 'background: yellow; border: 4px solid red;')",
                        element);
            }
            ThreadManager.setPreviousElement(element);

            Point elementLocation = element.getLocation();
            ((JavascriptExecutor) driver).executeScript("window.scrollTo("
                    + "0," + (elementLocation.getY() - 150) + ");");
            return element;
        } else {
            throw new NoSuchElementException("Unable to find element by specified locators");
        }
    }


    /**
     * @param fieldName
     * @param element
     * @throws Exception
     * @implNote JavaScript click which click on particular Web element using Js
     */

    public void jsClick(String fieldName, WebElement element) throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        try {
            getElement(element);
            explicitWaitForElementGetVisible(fieldName, element, "12");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Clicked on " + fieldName);

//            ThreadManager.getDetailReport().addStep("click", "Clicked on " + fieldName, Status.PASS);

        } catch (Throwable t) {

            logger.info(t.getClass().getName() + " -> Error while clicking on " + fieldName + "\n" + t);
//            ThreadManager.getDetailReport().addStep("click", "Error while clicking on " + fieldName + "<BR>" + t,
//                    Status.FAIL);
            Assert.fail("Error clicking on Element");

        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }


    public boolean isDisplayed(WebElement element, String elementName) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        boolean isDisplay = false;
        try {
            isDisplay = element.isDisplayed();
            if (!isDisplay) {

                logger.info("The element '{}' is not visible on the page.", elementName);
//                ThreadManager.getDetailReport().addStep(
//                        "Verify Element Displayed",
//                        "The element \"" + elementName + "\" is not visible on the page. This might cause issues in the user journey.",
//                        Status.FAIL
//                );
                Assert.fail("The element \"" + elementName + "\" is not displayed on the page.");
            } else {


                logger.info("The element '{}' is visible on the page.", elementName);
//                ThreadManager.getDetailReport().addStep(
//                        "Verify Element Displayed",
//                        "The element \"" + elementName + "\" is visible on the page.",
//                        Status.PASS
//                );
            }
        } catch (Exception e) {
            logger.info(elementName + " is not displayed due to an exception: " + e.getMessage());
            ThreadManager.getDetailReport().addStep("isDisplayed",
                    "Exception occurred while checking display status for " + elementName + ": "
                            + e.getMessage(), Status.FAIL);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        }
        return isDisplay;
    }


    public JSONObject stringToJon(String args) {
        System.out.println(args);
        JSONObject jsonObject = new JSONObject(args);

        return jsonObject;
    }

}
    