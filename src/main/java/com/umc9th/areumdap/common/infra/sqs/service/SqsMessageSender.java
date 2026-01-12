package com.umc9th.areumdap.common.infra.sqs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.infra.sqs.properties.EmailVerificationQueueProperties;
import com.umc9th.areumdap.common.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
@RequiredArgsConstructor
public class SqsMessageSender {

    private final SqsAsyncClient sqsClient;
    private final ObjectMapper objectMapper;
    private final EmailVerificationQueueProperties properties;

    public void publish(Object payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);

            String queueUrl = sqsClient.getQueueUrl(req ->
                    req.queueName(properties.queueName())
            ).join().queueUrl();

            sqsClient.sendMessage(req -> req
                    .queueUrl(queueUrl)
                    .messageBody(body)
            ).join();

        } catch (Exception e) {
            throw new GeneralException(
                    ErrorStatus.SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR
            );
        }
    }

}

