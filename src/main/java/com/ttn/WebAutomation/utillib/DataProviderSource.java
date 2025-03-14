package com.ttn.WebAutomation.utillib;

public class DataProviderSource {
    private static ThreadLocal<Object[][]> threadLocalData = new ThreadLocal<>();

    public static void setData(Object[][] data) {
        threadLocalData.set(data);
    }


}



