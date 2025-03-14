package com.ttn.WebAutomation.utillib;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class S3BucketConnection {

    private static Logger logger = Logger.getLogger(S3BucketConnection.class.getName());

    public void dumpDirectoryInS3(String directoryPath, String s3BucketPath) throws IOException {
        logger.info("Uploading directory " + directoryPath + " to S3");
        logger.info("S3 bucket path: " + s3BucketPath);

        String accessKey = GetPropertyValues.getGenericProperty("s3.access.key");
        String secretAccessKey = GetPropertyValues.getGenericProperty("s3.secret.access.key");
        String bucketName = GetPropertyValues.getGenericProperty("s3.bucket.name");
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);

        // Create a client connection based on credentials
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1") // Change to your region
                .build();

        File directory = new File(directoryPath);
        if (directory.exists()) {
            if (directory.isDirectory()) {
                uploadDirectory(s3client, bucketName, s3BucketPath, directory);
                logger.info("Directory uploaded successfully to S3");
            } else {
                // If it's a file, upload the file
                s3client.putObject(new PutObjectRequest(bucketName, s3BucketPath, directory));
                logger.info("File uploaded successfully to S3");
            }
        } else {
            logger.error("Directory does not exist or is not a directory: " + directoryPath);
        }
    }

    private void uploadDirectory(AmazonS3 s3client, String bucketName, String s3BucketPath, File directory) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String s3FilePath = s3BucketPath + "/" + file.getName();
                s3client.putObject(new PutObjectRequest(bucketName, s3FilePath, file));
                logger.info("File uploaded: " + s3FilePath);
            } else if (file.isDirectory()) {
                uploadDirectory(s3client, bucketName, s3BucketPath + "/" + file.getName(), file);
            }
        }
    }
}