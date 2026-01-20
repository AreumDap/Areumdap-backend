package com.umc9th.areumdap.domain.push.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FcmService {

    public String sendMessage(
            String token,
            String title,
            String body,
            Map<String, String> data
    ) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putAllData(data)
                .build();

        try {
            return FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new RuntimeException("FCM 메시지 전송 실패", e);
        }
    }
}
