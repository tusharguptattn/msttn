package com.ttn.WebAutomation.pageObjects.vahanDashboard;

import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.DirectoryManager;
import com.ttn.WebAutomation.utillib.ExcelToCsvConvert;
import com.ttn.WebAutomation.utillib.S3BucketConnection;
import com.ttn.WebAutomation.utillib.VariablesForRto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class RtoVahaanReports extends VariablesForRto {
    public static final String VAHAAN_REPORT_NOT_DOWNLOADED = "Vahaan report not downloaded {}";
    private final WebDriver driver;
    private SeleniumHelper helper;
    protected static Logger logger = LoggerFactory.getLogger(RtoVahaanReports.class);
    CommonUtility commonutility;
    Path path;

    // constructor
    public RtoVahaanReports(SeleniumHelper helper, WebDriver activeDriver) {
        this.driver = activeDriver;
        this.helper = helper;
        PageFactory.initElements(this.driver, this);
        commonutility = new CommonUtility(activeDriver);
    }


    public boolean verifyVahanReportDownload(JSONObject data) {
        boolean isverify = false;

        try {
            System.out.println(data.toString());
            List<String> rtoList1= getRtoList(data);
            for(int i=1;i<rtoList1.size();i++){
                applyVahanFilters(data, rtoList1.get(i));
            }
            isverify = true;
        } catch (Exception e) {
            logger.info(VAHAAN_REPORT_NOT_DOWNLOADED, e.getMessage());
        }
        return isverify;
    }



    public List<String> getRtoList(JSONObject jsonObject) {
        List<String> rtoListContain = new ArrayList<>();
        try {
            RtoVahaanPojo rtoVahaanPojo = new RtoVahaanPojo(jsonObject);
            helper.explicitWaitForElementGetVisible(VAHANLOGOSTRING, vahanlogo, "120");
            helper.click(STATE_DROPDOWN, stateDropdown);
            WebElement stateoption = driver.findElement(By.xpath(LI_CONTAINS_ID_J_IDT_AND_CONTAINS_TEXT + rtoVahaanPojo.getState() + "')]"));
            helper.jsClick(STATEOPTION, stateoption);
            helper.click("rtoDropdown", rtoDropdown);
            for (WebElement a : rtoList) {
                rtoListContain.add(a.getText());
            }
        }
        catch (Exception e){
            logger.info(ERROR_IN_APPLYING_FILTERS, e.getMessage());
        }
        return rtoListContain;
    }


    /**
     * Method to apply filters for Vahan report
     *
     * @param jsonObject
     * @throws Exception
     */


    public void applyVahanFilters(JSONObject jsonObject,String rto) throws Exception {
        try {
            RtoVahaanPojo rtoVahaanPojo = new RtoVahaanPojo(jsonObject);

            String yAxis = rtoVahaanPojo.getYaxis();
            String xAxis = rtoVahaanPojo.getXaxis();
            String yearType = rtoVahaanPojo.getYearType();
            String year = rtoVahaanPojo.getYear();
            String type = ACTUAL_VALUE;
            String state = rtoVahaanPojo.getState();
            String month = rtoVahaanPojo.getMonth();
            JSONArray vehicleCategory = rtoVahaanPojo.getVehicleCategory();
            String category = null;
            JSONArray vehicleClassFilterOptions = rtoVahaanPojo.getVehicleClassFilterOptions();
            JSONArray vehicleCategoryFilterOptions = rtoVahaanPojo.getVehicleCategoryFilterOptions();
            String storage = rtoVahaanPojo.getStorage();



            helper.explicitWaitForElementGetVisible(VAHANLOGOSTRING, vahanlogo, "120");
            helper.isElementExists(typeDropdown, TYPE_DROPDOWN);
            helper.click(TYPE_DROPDOWN, typeDropdown);
            WebElement typeoption = driver.findElement(By.xpath(LI_CONTAINS_ID_J_IDT_AND_TEXT + type + STRING));

            helper.jsClick(TYPEOPTION, typeoption);

            helper.focusMoveToElement(STATE_DROPDOWN, stateDropdown);
            helper.click(STATE_DROPDOWN, stateDropdown);
            WebElement stateoption = driver.findElement(By.xpath(LI_CONTAINS_ID_J_IDT_AND_CONTAINS_TEXT + state + "')]"));
            helper.jsClick(STATEOPTION, stateoption);

            helper.click(RTO_DROPDOWN, rtoDropdown);
            WebElement rtooption = driver.findElement(By.xpath(LI_CONTAINS_ID_SELECTED_RTO_AND_CONTAINS_TEXT + rto + "')]"));
            helper.jsClick(RTOOPTION, rtooption);


            commonutility.waitForPageLoad(10);
            helper.explicitWaitForElementGetVisible(YAXIS_DROPDOWN, yaxisDropdown, "120");
            helper.click(YAXIS_DROPDOWN, yaxisDropdown);
            WebElement yaxisoption = driver.findElement(By.xpath(LI_CONTAINS_ID_YAXIS_VAR_AND_TEXT + yAxis + STRING));

            helper.jsClick(YAXISOPTION, yaxisoption);
            commonutility.waitForPageLoad(10);
            helper.explicitWaitForElementGetVisible(XAXIS_DROPDOWN, xaxisDropdown, "120");
            helper.click(XAXIS_DROPDOWN, xaxisDropdown);
            WebElement xaxisoption = driver.findElement(By.xpath(LI_CONTAINS_ID_XAXIS_VAR_AND_TEXT + xAxis + STRING));

            helper.jsClick(XAXISOPTION, xaxisoption);
            commonutility.waitForPageLoad(10);
            selectXAxisSubFilters(xAxis, year, yearType);
            helper.jsClick(REFRESH_CTA, refreshCTA);
            commonutility.waitForPageLoad(5);
            loadertoDisappear();

            if ((vehicleCategory != null) && (!vehicleCategory.isEmpty())) {
                helper.jsClick("vehicleCategoryDropdown", vehicleCategoryDropdown);
                WebElement vehicleCategoryOption = driver.findElement(By.xpath("//li[contains(@id,'vchgroupTable:selectCatgGrp_') and text()='" + vehicleCategory + "']"));

                helper.jsClick("vehicleCategoryOption", vehicleCategoryOption);
            }



            applyFilterForMonthAndCategory(monthDropdown, MONTH_DROPDOWN, month, LI_CONTAINS_ID_GROUPING_TABLE_SELECT_MONTH_AND_TEXT, MONTH_OPTION);

            applyFilterForMonthAndCategory(categoryDropdown, CATEGORY_DROPDOWN, category, LI_CONTAINS_ID_GROUPING_TABLE_SELECT_CATG_TYPE_AND_TEXT, CATEGORY_OPTION);

            loadertoDisappear();
            helper.click(ARROW_FILTER_ICON, arrowFilterIcon);
            if ((vehicleClassFilterOptions != null) && (vehicleClassFilterOptions.length() != 0)) {


                for (var vehicleClassName : vehicleClassFilterOptions) {
                    WebElement vclassOption = driver.findElement(By.xpath(LABEL_TEXT + vehicleClassName + AND_CONTAINS_FOR_VH_CLASS));
                    helper.jsClick(VCLASS_OPTION, vclassOption);
                }


            }
            loadertoDisappear();
            if ((vehicleCategoryFilterOptions != null) && (vehicleCategoryFilterOptions.length() != 0)) {


                for (var vehicleCategoryName : vehicleCategoryFilterOptions) {
                    WebElement vcategoryOption = driver.findElement(By.xpath(LABEL_TEXT + vehicleCategoryName + AND_CONTAINS_FOR_VH_CATG));
                    helper.jsClick(CATEGORY_OPTION, vcategoryOption);
                }


            }
            helper.click(LEFTPANELFILTERREFRESHSTRING, leftpanelfilterrefresh);
            helper.click(FILTER_HIDE_ICON, filterHideIcon);
            loadertoDisappear();
            commonutility.waitForPageLoad(10);
            helper.isElementExists(downloadExcelFile, DOWNLOAD_EXCEL_FILE);
            String s3Dir = DirectoryManager.initializeThreadLocalDirectory(state, yAxis, xAxis, yearType, year);
            helper.click(DOWNLOAD_EXCEL_FILE, downloadExcelFile);
            commonutility.waitForPageLoad(10);
            String newFilename = null;
            if (yearType == null) {
                newFilename = "D:" + File.separator + VAHAN_REPORTS + File.separator + type.replaceAll("\\s", "") + File.separator + state.replaceAll("[ /]", "") + File.separator + yAxis.replaceAll("\\s", "") + File.separator + xAxis.replaceAll("\\s", "") + File.separator + year.replaceAll("\\s", "");
            } else {
                newFilename = "D:" + File.separator + VAHAN_REPORTS + File.separator + type.replaceAll("\\s", "") + File.separator + state.replaceAll("[ /]", "") + File.separator + yAxis.replaceAll("\\s", "") + File.separator + xAxis.replaceAll("\\s", "") + File.separator + yearType.replaceAll("\\s", "") + File.separator + year.replaceAll("\\s", "");
            }


            String fileName = REPORT_TABLE_XLSX;  // Change this based on the actual file name
            boolean fileDownloaded = isFileDownloaded(System.getProperty("user.dir")+"/Reports", fileName);
            Assert.assertTrue(fileDownloaded, FILE_WAS_NOT_DOWNLOADED_SUCCESSFULLY);
            long epochInMillisecond = Instant.now().toEpochMilli();
            File file = new File(newFilename);
            file.mkdirs();
            // If the file is downloaded, rename it
            renameFile(fileDownloaded, System.getProperty("user.dir")+"/Reports", fileName, newFilename, epochInMillisecond);

            if(storage.equalsIgnoreCase(S_3)){

                ExcelToCsvConvert excelToCsvConvert = new ExcelToCsvConvert();
                String csvName = excelToCsvConvert.convertExcelToCSV(new File(path.toString()));
                Path targetFilePath = Paths.get(s3Dir).resolve(Paths.get(csvName).getFileName().toString());
                Files.move(Paths.get(csvName), targetFilePath);
                Files.delete(path);
                String directoryPath = targetFilePath.toString().substring(0, targetFilePath.toString().lastIndexOf("/") + 1);
                directoryPath = directoryPath +rto +epochInMillisecond+ ".csv";
                Files.move(targetFilePath, Paths.get(directoryPath));
                S3BucketConnection s3BucketConnection = new S3BucketConnection();
                s3BucketConnection.dumpDirectoryInS3(directoryPath, DirectoryManager.directorySplit(directoryPath));
                Files.delete(Paths.get(directoryPath));
            }
        } catch (Exception e) {
            logger.info(ERROR_IN_APPLYING_FILTERS, e.getMessage());
        }


    }

    private void loadertoDisappear() {
        if (loaderIcon.isDisplayed()) {
            commonutility.waitForElementToDisappear(By.xpath(IMG_CONTAINS_SRC_PRELOADER_GIF));
        }
    }


    /**
     * Method to select the X axis sub filters
     *
     * @param xaxis
     * @param year
     * @param yearType
     * @throws Exception
     */


    private void selectXAxisSubFilters(String xaxis, String year, String yearType) throws Exception {
        WebElement yearOption;
        switch (xaxis) {
            case MONTH_WISE:
                String yearTypeDisabled = yearTypeDropdown.getAttribute(CLASS);
                Assert.assertTrue(yearTypeDisabled != null &&
                        yearTypeDisabled.contains(UI_STATE_DISABLED), YEAR_TYPE_IS_NOT_DISABLED_FOR_MONTH_WISE);
                //For X axix Month Wise fiter value,Year type is disabled
                helper.click(YEAR_DROPDOWN, yearDropdown);
                yearOption = driver.findElement(By.xpath(LI_CONTAINS_ID_SELECTED_YEAR_AND_TEXT + year + STRING));

                helper.jsClick(YEAROPTION, yearOption);

                break;
            case FINANCIAL_YEAR, CALENDAR_YEAR:
                helper.click(YEAR_DROPDOWN, yearDropdown2);
                yearOption = driver.findElement(By.xpath(LABEL_TEXT1 + year + STRING));

                helper.jsClick(YEAROPTION, yearOption);
                helper.isElementExists(yearCloseIcon, YEAR_CLOSE_ICON);
                helper.jsClick(YEAR_CLOSE_ICON, yearCloseIcon);
                break;

            default:
                helper.click(YEAR_TYPE_DROPDOWN, yearTypeDropdown);
                WebElement yeartypeoption = driver.findElement(By.xpath(
                        LI_CONTAINS_ID_SELECTED_YEAR_TYPE_AND_TEXT + yearType + STRING));
                helper.jsClick(YEARTYPEOPTION, yeartypeoption);
                helper.click(YEAR_DROPDOWN, yearDropdown);
                yearOption = driver.findElement(By.xpath(LI_CONTAINS_ID_SELECTED_YEAR_AND_TEXT + year + STRING));

                helper.jsClick(YEAROPTION, yearOption);
                break;
        }

    }

    /**
     * Method to check if the file is downloaded
     *
     * @param downloadDirectory
     * @param fileName
     * @return
     */

    public boolean isFileDownloaded(String downloadDirectory, String fileName) {
        Path filePath = Paths.get(downloadDirectory, fileName);
        return Files.exists(filePath);
    }


    /**
     * Method to apply filters for month and category
     *
     * @param element
     * @param fieldName
     * @param jsonData
     * @param xpath
     * @param fieldNameForOption
     */


    public void applyFilterForMonthAndCategory(WebElement element, String fieldName, String jsonData, String xpath, String fieldNameForOption) {
        try {
            if ((jsonData != null) && (!jsonData.isEmpty())) {
                helper.explicitWaitForElementGetVisible(fieldName, element, "40");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
                wait.until(ExpectedConditions.elementToBeClickable(element));
//                TakesScreenshot scrShot = ((TakesScreenshot) driver);
//                File SrcFile = scrShot.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
//                Files.move(SrcFile.toPath(), Paths.get(System.getProperty("user.dir") + "/Screenshots.png"));
                helper.jsClick(fieldName, element);
                WebElement option = driver.findElement(By.xpath(xpath + jsonData + STRING));
                helper.jsClick(fieldNameForOption, option);
            }
        } catch (Exception e) {
            logger.info(ERROR_IN_APPLYING_FILTERS_FOR_MONTH_AND_CATEGORY, e.getMessage());
            Assert.fail(ERROR_IN_APPLYING_FILTERS_FOR_MONTH_AND_CATEGORY);
        }
    }


    /**
     * Method to rename the file
     *
     * @param fileDownloaded
     * @param downloadDirectory
     * @param fileName
     * @param newFilename
     * @param epochInMillisecond
     */

    public void renameFile(boolean fileDownloaded, String downloadDirectory, String fileName, String newFilename, long epochInMillisecond) {
        Path newFilePath = null;
        if (fileDownloaded) {
            Path oldFilePath = Path.of(downloadDirectory, fileName);
            newFilePath = Path.of(newFilename, REPORT_TABLE + epochInMillisecond + XLSX);
            // Rename the file
            try {
                Files.move(oldFilePath, newFilePath);
                logger.info(FILE_RENAMED_TO, newFilePath.getFileName());
                path = newFilePath;
            } catch (Exception e) {
                logger.info(ERROR_IN_RENAMING_THE_FILE, e.getMessage());
                Assert.fail(FAILED_TO_RENAME_THE_FILE);
            }

        }
    }
}

