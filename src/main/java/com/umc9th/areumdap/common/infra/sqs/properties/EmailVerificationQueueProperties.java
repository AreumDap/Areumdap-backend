package com.umc9th.areumdap.common.infra.sqs.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "areumdap.messaging.email-verification")
public record EmailVerificationQueueProperties(
        @NotBlank
        String queueName,

        @NotNull
        Poll poll
) {
    public record Poll(
            @Min(1)
            int maxMessages,

            @Min(0)
            int waitTimeSeconds
    ) {}

    @Override
    public String toString() {
        return "SqsProperties[" +
                "emailVerificationQueueName=" + queueName +
                ", maxMessages=" + poll.maxMessages +
                ", waitTimeSeconds=" + poll.waitTimeSeconds +
                ']';
    }
}
