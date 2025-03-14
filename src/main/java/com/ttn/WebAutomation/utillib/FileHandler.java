package com.ttn.WebAutomation.utillib;

import java.io.File;

public abstract class FileHandler {
    public static File fileCreated = null;


    /**
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName) {
        String result = "";
        File file = new File(fileName);

        if (file.getName().lastIndexOf('.') == -1) {
            return result;
        }

        int position = file.getName().lastIndexOf('.');

        result = file.getName().substring(position + 1);

        return result;
    }

}
