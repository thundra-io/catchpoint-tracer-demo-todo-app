package com.catchpoint.tracing.demo.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * @author sozal
 */
@Profile("!test")
@Configuration
public class AWSSDKConfiguration {

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.
                builder().
                build();
    }

}
