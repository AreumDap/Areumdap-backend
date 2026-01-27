package com.umc9th.areumdap.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendChatMessageRequest(
        @NotBlank(message = "메시지 내용은 필수입니다")
        String content,
        @NotNull(message = "채팅 스레드 Id는 필수입니다")
        Long userChatThreadId
) {
}
