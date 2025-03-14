package com.ttn.WebAutomation.utillib;

public class ObjectReader {

    // Creating reference of Config Reader
//    public static ConfigReader reader = new PropertyReader();

//}

    public static String getResourcePath(String path) {

        String basePath = System.getProperty("user.dir");
        return basePath + path;
    }

}