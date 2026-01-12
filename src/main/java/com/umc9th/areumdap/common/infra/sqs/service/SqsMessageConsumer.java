package com.umc9th.areumdap.common.infra.sqs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.base.BaseStatus;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.infra.sqs.dto.request.EmailVerificationMessageRequest;
import com.umc9th.areumdap.common.infra.sqs.properties.EmailVerificationQueueProperties;
import com.umc9th.areumdap.domain.auth.util.EmailSender;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageConsumer {

    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;
    private final EmailSender emailSender;
    private final SqsMessageDeduplicator sqsMessageDeduplicator;
    private final EmailVerificationQueueProperties properties;

    private String queueUrl;

    @PostConstruct
    void init() {
        this.queueUrl = sqsAsyncClient.getQueueUrl(req ->
                req.queueName(properties.queueName())
        ).join().queueUrl();
    }

    @Scheduled(fixedDelay = 3000)
    public void poll() {
        var poll = properties.poll();
        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(poll.maxMessages())
                .waitTimeSeconds(poll.waitTimeSeconds())
                .build();

        sqsAsyncClient.receiveMessage(request)
                .join()
                .messages()
                .forEach(this::handle);
    }

    private void handle(Message message) {
        String messageId = message.messageId();

        if(sqsMessageDeduplicator.alreadyProcessed(messageId)){
            delete(message);
            return;
        }

        try {
            EmailVerificationMessageRequest payload =
                    objectMapper.readValue(
                            message.body(),
                            EmailVerificationMessageRequest.class
                    );

            emailSender.sendEmailVerificationCode(payload.email(), payload.verificationCode());
            sqsMessageDeduplicator.markProcessed(messageId);
            delete(message);
        }catch (Exception e) {
            log.error("SQS email verification failed", e);
            throw new GeneralException((BaseStatus) e);
        }
    }

    private void delete(Message message) {
        sqsAsyncClient.deleteMessage(req -> req
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
        );
    }
}
