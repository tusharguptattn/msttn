package com.ttn.WebAutomation.utillib;

import com.rabbitmq.client.*;
import com.ttn.WebAutomation.pageObjects.vahanDashboard.RtoVahaanReports;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer {
    protected static Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private static final String QUEUE_NAME = "data";

    public void consumeData(SeleniumHelper helper, RtoVahaanReports reportsView) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("ec2-44-222-94-94.compute-1.amazonaws.com"); // Replace with your RabbitMQ server host
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.basicQos(1); // Ensures one unacknowledged message is delivered at a time
        logger.info("Waiting for messages from Queue.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            logger.info("Received message: {}", message);

            try {
                JSONObject jsonObject = helper.stringToJon(message);
                boolean result = reportsView.verifyVahanReportDownload(jsonObject);
                Assert.assertTrue(result, "UserJourneyToVerifyVahanReportDownload method is not verified successfully");
                // Manual acknowledgment after processing
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error("Error processing message: {}", e.getMessage());
                // Reject the message and requeue it
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

        // Keep the main thread alive to continue processing messages
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}