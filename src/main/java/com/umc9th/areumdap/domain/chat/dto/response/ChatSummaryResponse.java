package com.umc9th.areumdap.domain.chat.dto.response;

import java.time.LocalDateTime;

public record ChatSummaryResponse(
        String summaryContent,
        Long userChatThreadId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer durationMinutes,
        Integer messageCount
) {
}
