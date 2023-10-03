package com.catchpoint.tracing.demo.todo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * @author sozal
 */
@Configuration
@ConditionalOnProperty(value = "aws.sdk.enabled", havingValue = "true", matchIfMissing = true)
public class AWSSDKConfiguration {

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.
                builder().
                build();
    }

}
