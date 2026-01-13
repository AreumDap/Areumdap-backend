package com.umc9th.areumdap.common.infra.sqs.config;

import com.umc9th.areumdap.common.infra.sqs.properties.EmailVerificationQueueProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EmailVerificationQueueProperties.class)
public class SqsPropertiesConfig {
}
