package com.catchpoint.tracing.demo.todo.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.io.IOException;

/**
 * @author sozal
 */
@Service
public class NotificationService {

    @Autowired
    private SnsClient snsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String notificationTopicARN = System.getenv("TODO_APP_NOTIFICATION_TOPIC_ARN");

    public void sendNotification(NotificationEvent message) throws IOException {
        String messageStr = objectMapper.writeValueAsString(message);
        snsClient.publish(PublishRequest
                .builder()
                .topicArn(notificationTopicARN)
                .message(messageStr)
                .build());
    }

}
