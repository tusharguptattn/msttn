package com.ttn.WebAutomation.utillib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DirectoryManager {

    DirectoryManager() {
        // private constructor to prevent instantiation
    }

    protected static Logger logger = LoggerFactory.getLogger(DirectoryManager.class);
    // ThreadLocal variable to store the download directory for each thread
    private static ThreadLocal<String> threadLocalDirectory = ThreadLocal.withInitial(() -> createDownloadDirectory());

    private static ThreadLocal<String> threadLocalDirectoryS3 = ThreadLocal.withInitial(() -> createDownloadDirectory());

    // Method to create a unique download directory for each thread
    private static String createDownloadDirectory() {
        try {
            long epochInMillisecond = Instant.now().toEpochMilli();
            Path dirPath = Paths.get(System.getProperty("user.dir"), "Reports","report"+epochInMillisecond);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            return dirPath.toString();
        } catch (Exception e) {
            logger.info("Error creating download directory: {} ", e.getMessage());
        }
        return null;
    }

    // Get the download directory for the current thread
    public static String getDownloadDirectory() {
        System.out.println("Thread name under download directory"+Thread.currentThread().getName());
        System.out.println("ThreadLocalDirectory: " + threadLocalDirectory.get());
        return threadLocalDirectory.get();
    }

    // Clean up the thread-local variable after the test
    public static void cleanup() {
        threadLocalDirectory.remove();
    }

    private static String createDownloadDirectory(String state, String yAxis, String xAxis, String yearType, String year) {
        try {
            // Get current date components
            java.time.LocalDate currentDate = java.time.LocalDate.now();
            String currentYear = String.valueOf(currentDate.getYear());
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
            String currentMonth = currentDate.format(monthFormatter);
            String currentDay = String.format("%02d", currentDate.getDayOfMonth());
            String formattedDate = currentDay+"-"+currentMonth+"-"+currentYear;

            // Construct the directory path
            Path dirPath = Paths.get(System.getProperty("user.dir"), currentYear, currentMonth, formattedDate, state);

            // Create directories if they do not exist
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            return dirPath.toString();
        } catch (Exception e) {
            logger.info("Error creating download directory: {} ", e.getMessage());
        }
        return null;
    }

    public static String initializeThreadLocalDirectory(String state, String yAxis, String xAxis, String yearType, String year) {
        threadLocalDirectoryS3 = ThreadLocal.withInitial(() -> createDownloadDirectory(state, yAxis, xAxis, yearType, year));
        return threadLocalDirectoryS3.get();
    }




    public static String directorySplit(String directoryPath) {
//        String path = "/Users/tushargupta/Downloads/MSIL_Live_Sales_Dashboard/2025/02/27022025/Rajasthan/reportTable1740636018001.csv";

        // Regular expression to match the year (4 digits)
        String regex = "/(\\d{4})/";

        // Split the path into two parts: before the year and after the year (including the year)
        String[] parts = directoryPath.split(regex, 2); // Split into 2 parts only
        String afterYear = null;
        if (parts.length > 1) {
            // Extract the year from the original path
            String year = directoryPath.substring(parts[0].length() + 1, parts[0].length() + 5); // Extract the 4-digit year

            System.out.println("Year: " + year);

            // Reconstruct the path before and after the year
            afterYear = year + "/" + parts[1]; // Include the year in the "after year" path
        } else {
            System.out.println("Year not found in the path.");
        }
        return afterYear;
    }
}