package com.ttn.WebAutomation.tests.msil.vahanReportView;

import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.pageObjects.vahanDashboard.RtoVahaanReports;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.RabbitMQConsumer;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DownloadVahanReportTest extends BaseLib {


    private String data; // Make sure this variable holds the correct data passed from DataProvider

    public DownloadVahanReportTest(String data) {
        this.data = data;
    }


    //    @Factory(dataProvider = "filterInputsRTO", dataProviderClass = DataProviderSource.class)
    public Object[] createTestInstances(String data) {
        return new Object[]{new DownloadVahanReportTest(data)};
    }
    @Test(groups = {"sanity"}, enabled = true)
    public void DownloadRTOReportByState() throws IOException, TimeoutException, InterruptedException {

        WebDriver driver = BaseLib.getActiveDriver();
        SeleniumHelper helper = BaseLib.getHelperPage(driver);
        RtoVahaanReports reportsView = BaseLib.getRtoVahanReportsPage(helper, driver);
        RabbitMQConsumer consumer = new RabbitMQConsumer();
        consumer.consumeData(helper, reportsView);
//        JSONObject jsonObject = helper.stringToJon(data);
//        boolean result = reportsView.verifyVahanReportDownload(jsonObject);
//        Assert.assertTrue(result, "UserJourneyToVerifyVahanReportDownload method is not verified successfully");
    }


}
