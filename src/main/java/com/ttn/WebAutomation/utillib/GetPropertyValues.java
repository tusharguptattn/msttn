package com.ttn.WebAutomation.utillib;

import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.FrameworkException;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetPropertyValues {


    private static Properties genericProperties;
    private static Properties environmentProperties;

    private static Logger logger
            = Logger.getLogger(GetPropertyValues.class.getName());

    /**
     * default constructor, it reads configuration
     * properties from the default location i.e.
     * in build environment from ./conf folder &
     * in development environment from resources folder
     *
     * @throws Exception
     */
    public GetPropertyValues() throws FrameworkException {
        this(null, null);
    }

    /**
     * Constructor to read global properties and
     * global object repository properties
     * from the given file path
     *
     * @param globalPropertiesFile
     * @param globalORFile
     * @param errorCategoriesFile
     * @throws FrameworkException
     */
    InputStream inputStream = null;

    public GetPropertyValues(String genericPropertiesFile, String environmentPropertiesFile)
            throws FrameworkException {

        genericProperties = new Properties();
        environmentProperties = new Properties();

        try {
            if (genericPropertiesFile == null) {
                if (new File("./src/main/java/com/ttn/WebAutomation/properties/generic.properties").exists()) {
                    try {
                        inputStream = new FileInputStream(new File(
                                "./src/main/java/com/ttn/WebAutomation/properties/generic.properties"));
                    } catch (Exception e) {
                        logger.info("FileInputStream Exception");

                    }


                } else {
                    inputStream = getClass().getClassLoader().getResourceAsStream(
                            "generic.properties");
                }
            } else {
                try {
                    inputStream = new FileInputStream(new File(genericPropertiesFile));

                } catch (Exception e) {
                    logger.info("FileInputStream Exception");
                }

            }
            genericProperties.load(inputStream);
            logger.info("Loaded generic properties successfully");
        } catch (Exception e) {

            logger.log(Level.SEVERE, "Unable to load global properties from \""
                    + genericPropertiesFile + "\"", e);
            throw new FrameworkException(e.getClass().getName()
                    + " -> Unable to load global properties from \""
                    + genericPropertiesFile + "\"", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.info("IOException");
            }

        }
        try {
            if (environmentPropertiesFile == null) {
                if (new File("./" + BaseLib.globalEnvironment.toLowerCase() + "_config.properties").exists()) {
                    System.out.println("outside getResourceAsStream");
                    inputStream = new FileInputStream(new File(
                            "./" + BaseLib.globalEnvironment.toLowerCase() + "_config.properties"));
                } else {
                    System.out.println("inside getResourceAsStream");
                    inputStream = getClass().getClassLoader().getResourceAsStream(
                            BaseLib.globalEnvironment.toLowerCase() + "_config.properties");
                    System.out.println("inputStream>>" + inputStream);
                }
            } else {
                inputStream = new FileInputStream(new File(environmentPropertiesFile));
            }
            environmentProperties.load(inputStream);
            logger.info("Loaded environment properties successfully");
        } catch (Exception e) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                logger.info("IOException");
            }
            logger.log(Level.SEVERE, "Unable to load environment properties from \""
                    + environmentProperties + "\"", e);
            throw new FrameworkException(e.getClass().getName()
                    + " -> Unable to load environment properties from \""
                    + environmentProperties + "\"", e);
        }
    }

    public static String getPropertyValues(String file, String key) {
        String value = "";
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(file)));
            value = prop.getProperty(key);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            logger.info("FileNotFoundException");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.info("FileNotFoundException");
        }
        return value;
    }

    /**
     * Get value of given global/dynamic property name
     *
     * @param propertyName
     * @return
     * @throws Exception
     * @throws FrameworkException in case property doesn't exists
     */
    public static String getGenericProperty(String propertyName)
            throws FrameworkException {
        String propertyValue = genericProperties.getProperty(propertyName);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            throw new FrameworkException("Generic properties not found with name \""
                    + propertyName + "\"");
        }
    }

    /**
     * Get value of given global/dynamic property name
     *
     * @param propertyName
     * @return
     * @throws Exception
     * @throws FrameworkException in case property doesn't exists
     */
    public static String getEnvironmentProperty(String propertyName)
            throws FrameworkException {
        String propertyValue = environmentProperties.getProperty(propertyName);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            throw new FrameworkException("Environment properties not found with name \""
                    + propertyName + "\"");
        }
    }
}
