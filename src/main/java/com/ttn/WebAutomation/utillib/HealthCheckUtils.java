package com.ttn.WebAutomation.utillib;

import com.ttn.WebAutomation.base.BaseLib;

import java.io.IOException;
import java.net.*;

/**
 * @author Akhil
 * @implNote Checks the health of WEB URL and DB Connection
 * @see BaseLib
 * @since 29/04/2023
 */

public class HealthCheckUtils {


    // encoded url healthcheclk
    public static boolean checkUrlHealth(String targetUrl) {
        try {
            // Set up Authenticator to handle credentials with special characters
            String username = GetPropertyValues.getEnvironmentProperty("username");
            String password = GetPropertyValues.getEnvironmentProperty("password");
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });

            // Open the URL connection with URL class :)
            URL url = new URL(targetUrl);
            System.out.println(targetUrl);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(2000);

            int responseCode = httpUrlConnection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            if (e instanceof UnknownHostException || e instanceof java.net.SocketTimeoutException) {

                return false;
            } else {

                return false;
            }
        }
    }

}