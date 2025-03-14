package com.ttn.WebAutomation.utillib;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class VariablesForRto {


    // Xpaths

    @FindBy(xpath = "//img[contains(@src,'logoo.png')]")
    public WebElement vahanlogo;
    @FindBy(xpath = "//label[contains(@id,'j_idt') and text()='Actual Value']")
    public WebElement typeDropdown;
    @FindBy(xpath = "(//label[text()='State:']//parent::label)[1]//following-sibling::div/label")
    public WebElement stateDropdown;
    @FindBy(xpath = "//label[@id='selectedRto_label']")
    public WebElement rtoDropdown;

    @FindBy(xpath = "//label[@id='xaxisVar_label']")
    public WebElement xaxisDropdown;
    @FindBy(xpath = "//div[@id='selectedYearType']")
    public WebElement yearTypeDropdown;
    @FindBy(xpath = "//label[@id='selectedYear_label']")
    public WebElement yearDropdown;
    @FindBy(xpath = "//label[@id='yaxisVar_label']")
    public WebElement yaxisDropdown;
    @FindBy(xpath = "(//span[@class='ui-button-text ui-c'][normalize-space()='Refresh'])[1]")
    public WebElement refreshCTA;
    @FindBy(xpath = "//img[contains(@src,'preloader.gif')]")
    public WebElement loaderIcon;
    @FindBy(xpath = "//img[@title='Download EXCEL file']")
    public WebElement downloadExcelFile;
    @FindBy(xpath = "//label[@id='vchgroupTable:selectCatgGrp_label']")
    public WebElement vehicleCategoryDropdown;
    @FindBy(xpath = "//label[@id='groupingTable:selectMonth_label']")
    public WebElement monthDropdown;
    @FindBy(xpath = "//label[@id='groupingTable:selectCatgType_label']")
    public WebElement categoryDropdown;
    @FindBy(xpath = "//label[@id='yearList_label']")
    public WebElement yearDropdown2;
    @FindBy(xpath = " //span[@class='ui-icon ui-icon-circle-close']")
    public WebElement yearCloseIcon;
    @FindBy(xpath = "//li[contains(@id,'selectedRto')]")
    public List<WebElement> rtoList;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-arrow-4-diag']")
    public WebElement arrowFilterIcon;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-triangle-1-w']")
    public WebElement filterHideIcon;
    @FindBy(xpath = "(//span[@class='ui-button-text ui-c'][normalize-space()='Refresh'])[2]")
    public WebElement leftpanelfilterrefresh;



    // Variables

    public static final String YEAROPTION = "yearoption";

    public static final String YEAR_DROPDOWN = "YearDropdown";

    public static final String VAHANLOGOSTRING = "vahanlogo";
    public static final String TYPE_DROPDOWN = "typeDropdown";
    public static final String TYPEOPTION = "typeoption";
    public static final String STATE_DROPDOWN = "stateDropdown";
    public static final String STATEOPTION = "stateoption";
    public static final String RTO_DROPDOWN = "rtoDropdown";
    public static final String RTOOPTION = "rtooption";
    public static final String YAXIS_DROPDOWN = "yaxisDropdown";
    public static final String YAXISOPTION = "yaxisoption";
    public static final String XAXIS_DROPDOWN = "xaxisDropdown";
    public static final String XAXISOPTION = "xaxisoption";
    public static final String REFRESH_CTA = "refreshCTA";
    public static final String MONTH_DROPDOWN = "monthDropdown";
    public static final String MONTH_DROPDOWN1 = "MonthDropdown";
    public static final String MONTH_OPTION = "monthOption";
    public static final String CATEGORY_DROPDOWN = "categoryDropdown";
    public static final String CATEGORY_OPTION = "categoryOption";
    public static final String ARROW_FILTER_ICON = "arrowFilterIcon";
    public static final String VCLASS_OPTION = "vclassOption";
    public static final String LEFTPANELFILTERREFRESHSTRING = "leftpanelfilterrefresh";
    public static final String FILTER_HIDE_ICON = "filterHideIcon";
    public static final String DOWNLOAD_EXCEL_FILE = "downloadExcelFile";
    public static final String VAHAN_REPORTS = "vahan_reports";
    public static final String REPORT_TABLE_XLSX = "reportTable.xlsx";
    public static final String REPORT_TABLE = "reportTable";
    public static final String XLSX = ".xlsx";
    public static final String FILE_RENAMED_TO = "File renamed to: {}";
    public static final String FAILED_TO_RENAME_THE_FILE = "Failed to rename the file.";
    public static final String MONTH_WISE = "Month Wise";
    public static final String FINANCIAL_YEAR = "Financial Year";
    public static final String CALENDAR_YEAR = "Calendar Year";
    public static final String YEAR_CLOSE_ICON = "yearCloseIcon";
    public static final String YEAR_TYPE_DROPDOWN = "yearTypeDropdown";
    public static final String YEARTYPEOPTION = "yeartypeoption";

    public static final String LI_CONTAINS_ID_J_IDT_AND_TEXT = "//li[contains(@id,'j_idt') and text()='";
    public static final String LI_CONTAINS_ID_J_IDT_AND_CONTAINS_TEXT = "//li[contains(@id,'j_idt') and contains(text(),'";
    public static final String LI_CONTAINS_ID_SELECTED_RTO_AND_CONTAINS_TEXT = "//li[contains(@id,'selectedRto') and contains(text(),'";
    public static final String LI_CONTAINS_ID_YAXIS_VAR_AND_TEXT = "//li[contains(@id,'yaxisVar_') and text()='";
    public static final String LI_CONTAINS_ID_XAXIS_VAR_AND_TEXT = "//li[contains(@id,'xaxisVar_') and text()='";
    public static final String LI_CONTAINS_ID_GROUPING_TABLE_SELECT_MONTH_AND_TEXT = "//li[contains(@id,'groupingTable:selectMonth_') and text()='";
    public static final String LI_CONTAINS_ID_GROUPING_TABLE_SELECT_CATG_TYPE_AND_TEXT = "//li[contains(@id,'groupingTable:selectCatgType_') and text()='";
    public static final String LABEL_TEXT = "//label[text()='";
    public static final String AND_CONTAINS_FOR_VH_CLASS = "' and contains(@for,'VhClass:')]";
    public static final String AND_CONTAINS_FOR_VH_CATG = "' and contains(@for,'VhCatg:')]";
    public static final String IMG_CONTAINS_SRC_PRELOADER_GIF = "//img[contains(@src,'preloader.gif')]";
    public static final String UI_STATE_DISABLED = "ui-state-disabled";
    public static final String YEAR_TYPE_IS_NOT_DISABLED_FOR_MONTH_WISE = "Year Type is not disabled for Month wise";
    public static final String CLASS = "class";
    public static final String LI_CONTAINS_ID_SELECTED_YEAR_AND_TEXT = "//li[contains(@id,'selectedYear_') and text()='";
    public static final String LABEL_TEXT1 = "//div[@id='yearList_panel']//li/label[text()='";
    public static final String LI_CONTAINS_ID_SELECTED_YEAR_TYPE_AND_TEXT = "//li[contains(@id,'selectedYearType') and text()='";
    public static final String STRING = "']";


    public static final String ACTUAL_VALUE = "Actual Value";
    public static final String FILE_WAS_NOT_DOWNLOADED_SUCCESSFULLY = "File was not downloaded successfully.";
    public static final String S_3 = "s3";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String ERROR_IN_APPLYING_FILTERS = "Error in applying filters {}";
    public static final String ERROR_IN_APPLYING_FILTERS_FOR_MONTH_AND_CATEGORY = "Error in applying filters for month and category {}";
    public static final String ERROR_IN_RENAMING_THE_FILE = "Error in renaming the file {}";
}
