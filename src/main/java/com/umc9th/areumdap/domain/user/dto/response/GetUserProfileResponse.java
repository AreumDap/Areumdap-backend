package com.umc9th.areumdap.domain.user.dto.response;

import com.umc9th.areumdap.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetUserProfileResponse(
        Long userId,
        String name,
        LocalDate birth,
        boolean notificationEnabled,
        LocalTime pushNotificationTime
) {
    public static GetUserProfileResponse from(User user) {
        return new GetUserProfileResponse(
                user.getId(),
                user.getName(),
                user.getBirth(),
                user.isNotificationEnabled(),
                user.getNotificationTime()
        );
    }
}
