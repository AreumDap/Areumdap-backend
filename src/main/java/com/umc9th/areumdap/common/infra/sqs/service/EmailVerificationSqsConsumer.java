package com.umc9th.areumdap.common.infra.sqs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.infra.sqs.dto.request.EmailVerificationMessageRequest;
import com.umc9th.areumdap.common.infra.sqs.properties.EmailVerificationQueueProperties;
import com.umc9th.areumdap.common.infra.sqs.properties.SqsPollProperties;
import com.umc9th.areumdap.domain.auth.util.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
public class EmailVerificationSqsConsumer extends SqsMessageConsumer<EmailVerificationMessageRequest> {

    private final EmailSender emailSender;
    private final EmailVerificationQueueProperties properties;

    public EmailVerificationSqsConsumer(
            SqsAsyncClient sqsAsyncClient,
            ObjectMapper objectMapper,
            SqsMessageDeduplicator deduplicator,
            EmailSender emailSender,
            EmailVerificationQueueProperties properties
    ) {
        super(sqsAsyncClient, objectMapper, deduplicator);
        this.emailSender = emailSender;
        this.properties = properties;
    }

    @Override
    protected SqsPollProperties pollProperties() {
        return properties.poll();
    }

    @Override
    protected String queueName() {
        return properties.queueName();
    }

    @Override
    protected Class<EmailVerificationMessageRequest> payloadType() {
        return EmailVerificationMessageRequest.class;
    }

    @Override
    protected void handle(EmailVerificationMessageRequest payload) {
        emailSender.sendEmailVerificationCode(
                payload.email(),
                payload.verificationCode()
        );
        log.info("Email verification code sent");
    }

}
