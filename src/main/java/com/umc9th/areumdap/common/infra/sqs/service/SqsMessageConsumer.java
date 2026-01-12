package com.umc9th.areumdap.common.infra.sqs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.infra.sqs.properties.SqsPollProperties;
import com.umc9th.areumdap.common.status.ErrorStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@RequiredArgsConstructor
public abstract class SqsMessageConsumer<T> {

    protected final SqsAsyncClient sqsAsyncClient;
    protected final ObjectMapper objectMapper;
    protected final SqsMessageDeduplicator deduplicator;

    protected String queueUrl;

    @PostConstruct
    protected void initQueueUrl() {
        this.queueUrl = sqsAsyncClient.getQueueUrl(req ->
                req.queueName(queueName())
        ).join().queueUrl();
    }

    @Scheduled(fixedDelay = 3000)
    public void poll() {
        SqsPollProperties poll = pollProperties();

        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(poll.maxMessages())
                .waitTimeSeconds(poll.waitTimeSeconds())
                .build();

        sqsAsyncClient.receiveMessage(request)
                .join()
                .messages()
                .forEach(this::handleInternal);
    }

    // 큐에 맞게 로직 분리
    private void handleInternal(Message message) {
        if (deduplicator.alreadyProcessed(message.messageId())) {
            delete(message);
            return;
        }

        try {
            T payload = objectMapper.readValue(message.body(), payloadType());
            handle(payload);
            deduplicator.markProcessed(message.messageId());
            delete(message);
        } catch (Exception e) {
            log.error("SQS message handling failed", e);
            throw new GeneralException(ErrorStatus.SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR);
        }
    }

    // SQS에 쌓인 메시지 삭제
    protected void delete(Message message) {
        sqsAsyncClient.deleteMessage(req -> req
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
        );
    }

    // 상속받는 클래스가 구현
    protected abstract String queueName();
    protected abstract SqsPollProperties pollProperties();
    protected abstract Class<T> payloadType();
    protected abstract void handle(T payload);

}

