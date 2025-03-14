package com.ttn.WebAutomation.utillib;

import com.ttn.WebAutomation.base.BaseLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
//import com.ttn.WebAutomation.base.BaseTest;

public class PropertyReader extends BaseLib {

    static FileInputStream fip;

    public static Properties setup_properties() throws IOException {

        try {

            properties = new Properties();

            File fileName = new File(GlobalVariables.getPropertiesPath() + GlobalVariables.PROPERTY_FILE_NAME);
            fip = new FileInputStream(fileName);
            properties.load(fip);

        } catch (Exception e) {
            fip.close();
            System.out.println(e.getMessage());
        }
        return properties;

    }
}
