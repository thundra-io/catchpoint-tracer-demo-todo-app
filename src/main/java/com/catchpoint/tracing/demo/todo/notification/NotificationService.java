package com.catchpoint.tracing.demo.todo.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.IOException;

/**
 * @author sozal
 */
@Service
public class NotificationService {

    @Autowired
    private SqsClient sqsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String notificationQueueURL = System.getenv("TODO_APP_NOTIFICATION_QUEUE_URL");

    public void sendNotification(NotificationEvent message) throws IOException {
        String messageStr = objectMapper.writeValueAsString(message);
        sqsClient.sendMessage(SendMessageRequest
                .builder()
                .queueUrl(notificationQueueURL)
                .messageBody(messageStr)
                .build());
    }

}
