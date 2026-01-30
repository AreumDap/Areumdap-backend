package com.umc9th.areumdap.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatSummaryRequest(
        @NotNull(message = "채팅 스레드 Id는 필수입니다")
        Long userChatThreadId
) {
}
