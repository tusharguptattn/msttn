package com.ttn.WebAutomation.pageObjects.vahanDashboard;

import org.json.JSONArray;
import org.json.JSONObject;

public class RtoVahaanPojo {
    public static final String ADDITIONAL_FILTERS = "additional-filters";
    private String state;
    private String xaxis;
    private String year;
    private String yaxis;
    private String month;
    private String yearType;
    private JSONArray vehicleCategory;
    private JSONArray vehicleClassFilterOptions;
    private JSONArray vehicleCategoryFilterOptions;
    private String storage;
    private String storageBasePath;
    private String retryCountOnFailure;
    private String retryMailIdOnFailure;
    private JSONObject jsonObject=null;
    public RtoVahaanPojo(JSONObject data){

        jsonObject = data;
    }


    public String getState() {
        setState();
        return state;
    }

    public void setState() {
        this.state = jsonObject.getString("state");
    }


    public String getXaxis() {
        setXaxis();
        return xaxis;
    }

    public void setXaxis() {
        this.xaxis = jsonObject.getString("x-axis");
    }

    public String getYear() {
        setYear();
        return year;
    }

    public void setYear() {
        this.year = jsonObject.getString("year");
    }

    public String getYaxis() {
        setYaxis();
        return yaxis;
    }

    public void setYaxis() {
        this.yaxis = jsonObject.getString("y-axis");
    }

    public String getMonth() {
        setMonth();
        return month;
    }

    public void setMonth() {
        this.month = jsonObject.getString("month");
    }

    public String getYearType() {
        setYearType();
        return yearType;
    }

    public void setYearType() {
        this.yearType = jsonObject.getString("year-type");
    }

    public JSONArray getVehicleCategory() {
        setVehicleCategory();
        return vehicleCategory;
    }

    public void setVehicleCategory() {
        this.vehicleCategory = jsonObject.getJSONObject(ADDITIONAL_FILTERS).getJSONArray("vehicle-category");
    }

    public JSONArray getVehicleClassFilterOptions() {
        setVehicleClassFilterOptions();
        return vehicleClassFilterOptions;
    }

    public void setVehicleClassFilterOptions() {
        this.vehicleClassFilterOptions = jsonObject.getJSONObject(ADDITIONAL_FILTERS).getJSONArray("vehicleClassFilterOptions");
    }

    public JSONArray getVehicleCategoryFilterOptions() {
        setVehicleCategoryFilterOptions();
        return vehicleCategoryFilterOptions;
    }

    public void setVehicleCategoryFilterOptions() {
        this.vehicleCategoryFilterOptions = jsonObject.getJSONObject(ADDITIONAL_FILTERS).getJSONArray("vehicleCategoryFilterOptions");
    }

    public String getStorage() {
        setStorage();
        return storage;
    }

    public void setStorage() {
        this.storage = jsonObject.getString("storage");
    }

    public String getStorageBasePath() {
        setStorageBasePath();
        return storageBasePath;
    }

    public void setStorageBasePath() {
        this.storageBasePath = jsonObject.getString("storage-base-path");
    }

    public String getRetryCountOnFailure() {
        setRetryCountOnFailure();
        return retryCountOnFailure;
    }

    public void setRetryCountOnFailure() {
        this.retryCountOnFailure = jsonObject.getString("retry-count-on-failure");
    }

    public String getRetryMailIdOnFailure() {
        setRetryMailIdOnFailure();
        return retryMailIdOnFailure;
    }

    public void setRetryMailIdOnFailure() {
        this.retryMailIdOnFailure = jsonObject.getString("retry-mail-id-on-failure");
    }
}

