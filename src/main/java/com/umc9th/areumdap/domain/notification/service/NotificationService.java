package com.umc9th.areumdap.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class NotificationService {

    public String sendPushAlarm(
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
            throw new GeneralException(ErrorStatus.NOTIFICATION_SENDING_INTERNAL_SERVER_ERROR);
        }
    }
}
