package com.umc9th.areumdap.domain.chat.dto.request;

import jakarta.validation.constraints.Min;

import java.time.OffsetDateTime;

public record UserChatThreadCursorRequest(
        OffsetDateTime cursorTime,
        Long cursorId,

        @Min(1)
        int size
) {
}
