package com.umc9th.areumdap.domain.notification.dto.response;

public record NotificationTargetUser(
        Long userId,
        String deviceToken
) {
}
