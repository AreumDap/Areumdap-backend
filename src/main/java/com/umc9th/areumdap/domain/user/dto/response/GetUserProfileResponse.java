package com.umc9th.areumdap.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc9th.areumdap.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetUserProfileResponse(
        Long userId,
        String name,
        LocalDate birth,
        boolean notificationEnabled,

        @Schema(
                description = "알림 시간 (HH:mm)",
                example = "22:00",
                type = "string",
                format = "time"
        )
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
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
