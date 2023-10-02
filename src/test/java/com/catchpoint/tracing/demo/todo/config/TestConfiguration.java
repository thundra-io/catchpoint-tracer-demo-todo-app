package com.catchpoint.tracing.demo.todo.config;

import com.catchpoint.tracing.demo.todo.notification.NotificationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author sozal
 */
@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public NotificationService notificationService() {
        return Mockito.mock(NotificationService.class);
    }

}
