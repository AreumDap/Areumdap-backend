package com.umc9th.areumdap.domain.chat.dto.response;

public record CreateChatThreadResponse(
        String content,
        Long userChatThreadId
) {
}
