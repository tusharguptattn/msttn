package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GlobalVariables {

    private static File directory = new File(".");
    public static String PROPERTY_FILE_NAME = "generic.properties";
    public static String LOG4J_FILE_NAME = "log4j.properties";
    public static String REPORT_NAME = "";
    public static String REPORT_PATH = "";
    public static String RESULT_BASE_LOCATION = "";

    public static ArrayList<String> TEST_RESULT_COUNT = new ArrayList<>();

    public static String getPropertiesPath() throws IOException {
        return directory.getCanonicalPath() + File.separator + "src/main/java/com/ttn/WebAutomation/properties"
                + File.separator;
    }


}
