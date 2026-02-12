package com.umc9th.areumdap.common.infra.sqs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.infra.sqs.properties.EmailVerificationQueueProperties;
import com.umc9th.areumdap.common.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageSender {

    private final SqsAsyncClient sqsClient;
    private final ObjectMapper objectMapper;
    private final EmailVerificationQueueProperties properties;

    private final ConcurrentMap<String, String> queueUrlCache = new ConcurrentHashMap<>();

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
            log.error("SQS 메시지 발송 실패: {}", e.getMessage(), e);
            throw new GeneralException(
                    ErrorStatus.SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR
            );
        }
    }

}

