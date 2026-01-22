package com.umc9th.areumdap.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record UpdateUserNotificationSettingRequest(
        @Schema(description = "알림 활성화 여부", example = "true")
        boolean notificationEnabled,

        @Schema(
                description = "알림 시간 (HH:mm)",
                example = "22:00",
                type = "string",
                format = "time"
        )
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime notificationTime
) {
}
