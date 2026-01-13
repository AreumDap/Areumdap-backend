package com.umc9th.areumdap.common.infra.sqs.listener;

import com.umc9th.areumdap.common.infra.sqs.dto.request.EmailVerificationMessageRequest;
import com.umc9th.areumdap.common.infra.sqs.service.SqsMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationSqsEventListener {

    private final SqsMessageSender sqsMessageSender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmailVerificationMessageRequest request){
        sqsMessageSender.publish(
                new EmailVerificationMessageRequest(
                        request.email(),
                        request.verificationCode()
                )
        );
    }
}
