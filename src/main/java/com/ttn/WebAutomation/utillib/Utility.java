package com.ttn.WebAutomation.utillib;

import com.ttn.WebAutomation.base.BaseLib;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;


/**
 * this class contains various utility methods
 *
 * @author TTN
 */
public class Utility {


    /**
     * generates unique string which is based
     * of current date/time format "ddMMyyyhhmmssSS"
     * e.g. 22022017052848433
     *
     * @return
     */
    public static String generateUniqueString() {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("ddMMyyyhhmmssSS");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            logger.info("Error occured during wait"
                    + ", while getting unique string", e);
        }
        Date date = new Date();
        return simpleDateFormat.format(date);
    }


    public static void sendMailWithReport() {

        final String emailIdFrom = GetPropertyValues.getGenericProperty("emailIdFrom");

        final String passwordFrom = GetPropertyValues.getGenericProperty("passwordFrom");

        String emailIdSentTo = GetPropertyValues.getGenericProperty("emailIdTo");

        String rpLaunchURL = GetPropertyValues.getGenericProperty("rpLaunchURL");

        String env = BaseLib.globalEnvironment;

        String testype = StringUtils.capitalize(BaseLib.testingType);


        GlobalVariables.REPORT_NAME = FilenameUtils.getName(GlobalVariables.REPORT_PATH);

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.socketFactory.port", "465");

        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.port", "465");

        props.put("mail.smtp.ssl.checkserveridentity", true);


        Session session = null;

        try {

            session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(emailIdFrom, passwordFrom);

                }

            });

        } catch (Exception e) {

            logger.info(e);
        }


        try {

            String ScreenAttached = "";

            String url = GetPropertyValues.getEnvironmentProperty("testURL");
            ;


            Message message = new MimeMessage(session);


            message.setFrom(new InternetAddress(emailIdFrom));


            LocalDateTime currentDateTime = LocalDateTime.now();


            // Format current date and time

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String formattedDateTime = currentDateTime.format(formatter);


            String subject = GetPropertyValues.getGenericProperty("emailSubject");

            // Append current date and time to the existing subject

            String updatedSubject = subject + " (" + BaseLib.browserName + ") " + " [" + formattedDateTime + "]";


            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailIdSentTo));

            message.setSubject(updatedSubject);


            BodyPart messageBodyPart1 = new MimeBodyPart();

            // Set the body of email

            int pass = 0, warning = 0, skip = 0, fail = 0, error = 0;


            for (String value : GlobalVariables.TEST_RESULT_COUNT) {

                if (value.equalsIgnoreCase("pass"))

                    pass++;

                if (value.equalsIgnoreCase("warning"))

                    warning++;

                if (value.equalsIgnoreCase("skip"))

                    skip++;

                if (value.equalsIgnoreCase("fail"))

                    fail++;

                if (value.equalsIgnoreCase("error"))

                    error++;

            }

            messageBodyPart1.setContent("" + "<h3>Hi All,</h3>\n\n" + "\n\n"

                            + "<h3>This is a Nexa Revamp -Web" + " " + env + " " + testype + " " + "Test Automation Execution Report" + ScreenAttached + "</h3> \r\n" + "\r\n" + "\r\n"


                            + "URL - " + url + "<BR><BR>"

                            + "RP Launch URL - " + rpLaunchURL + "<BR><BR>"

                            + "<!DOCTYPE html>\n"

                            + "<html>\n"

                            + "<head>\n" +

                            "<style>\n" +

                            "table { width:30%;    }\n" +

                            "table, th, td { border: 1px solid black;  border-collapse: collapse;}\n" +

                            "td { padding: 2px;  text-align: left;}" +

                            "th {padding: 2px;  text-align: center;}\n" +

                            "table#t01 tr:nth-child(even) {background-color: #eee;}\n" +

                            "table#t01 tr:nth-child(odd) {background-color: #fff;}\n" +

                            "table#t01 th {background-color: lightblue; color: black;}\n" +

                            "</style>\n" +

                            "</head>\n" +

                            "<body>\n" +

                            "<h3>AUTOMATION EXECUTION</h3>\n" +

                            "<table id=\"t01\">\n" +

                            "  <tr>\n" +

                            "    <th colspan=\"2\">TEST EXECUTION SUMMARY</th>\n" +

                            "   </tr>\n" +

                            "  <tr>\n" +

                            "    <td><style=\"color:black;\">TOTAL TEST-SCRIPTS : </td>\n" +

                            "    <td>" + GlobalVariables.TEST_RESULT_COUNT.size() + "</td>\n" +

                            "   </tr>\n" +

                            "  <tr>\n" +

                            "    <td style=\"color:green;\">PASS :</td>\n" +

                            "    <td style=\"color:green;\">" + pass + "</td>\n" +

                            "  </tr>\n" +

                            "  <tr>\n" +

                            "    <td style=\"color:red;\">FAIL :</td>\n" +

                            "    <td style=\"color:red;\">" + fail + "</td>\n" +

                            "  </tr>\n" +

                            "  \n" +

                            "  <tr>\n" +

                            "    <td style=\"color:orange;\">WARNING : </td>\n" +

                            "    <td style=\"color:orange;\">" + warning + "</td>\n" +

                            "  </tr>\n" +

                            "   <tr>\n" +

                            "    <td style=\"color:gray;\">SKIP : </td>\n" +

                            "    <td style=\"color:gray;\">" + skip + "</td>\n" +

                            "  </tr>\n" +

                            "   <tr>\n" +

                            "    <td style=\"color:Tomato;\">ERROR : </td>\n" +

                            "    <td style=\"color:Tomato;\">" + error + "</td>\n" +

                            "  </tr>\n" +

                            "</table>\n" +

                            "</body>\n" +

                            "</html>\n" + "<BR>"

                            + "<h3>Please refer to the attached report for execution details and Kindly unzip attached file.</h3>"

                            + "Thanks!!",

                    "text/html");


            String fielname = GlobalVariables.REPORT_PATH;


            ZipDir.zip(fielname);
            String filename1 = fielname.substring(0, fielname.length() - 5) + ".zip";

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            DataSource source = new FileDataSource(filename1);

            messageBodyPart2.setDataHandler(new DataHandler(source));

            messageBodyPart2.setFileName(GlobalVariables.REPORT_NAME.substring(0, GlobalVariables.REPORT_NAME.length() - 5) + ".zip");


            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart2);

            multipart.addBodyPart(messageBodyPart1);

            message.setContent(multipart);

            Transport.send(message);
            logger.info("== Email Sent Successfully to ==" + emailIdSentTo);


        } catch (MessagingException e) {

            throw new RuntimeException(e);

        }

    }

    private static Logger logger
            = Logger.getLogger(Utility.class.getName());

}