package com.umc9th.areumdap.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional
public class NotificationCommandService {

    public String sendPushAlarm(
            String token,
            String title,
            String body,
            Map<String, String> data) {
        Message.Builder messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build());

        if (data != null) {
            messageBuilder.putAllData(data);
        }

        try {
            return FirebaseMessaging.getInstance().send(messageBuilder.build());
        } catch (Exception e) {
            log.error("알림 전송 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
