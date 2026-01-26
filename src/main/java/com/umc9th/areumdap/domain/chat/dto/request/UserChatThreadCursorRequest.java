package com.umc9th.areumdap.domain.chat.dto.request;

import java.time.OffsetDateTime;

public record UserChatThreadCursorRequest(
        OffsetDateTime cursorTime,
        Long cursorId,
        int size
) {
}
