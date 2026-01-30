package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.domain.chatbot.dto.response.ChatSummaryContentDto;

import java.time.LocalDateTime;

public record ChatSummaryResponse(
        ChatSummaryContentDto summaryContent,
        Long userChatThreadId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer durationMinutes,
        Integer messageCount
) {
}
