package com.catchpoint.tracing.demo.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

/**
 * @author sozal
 */
@Configuration
public class AWSSDKConfiguration {

    @Bean
    public SnsClient snsClient() {
        return SnsClient.
                builder().
                build();
    }

}
