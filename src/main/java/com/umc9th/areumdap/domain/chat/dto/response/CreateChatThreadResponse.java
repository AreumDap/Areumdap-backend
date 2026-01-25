package com.umc9th.areumdap.domain.chat.dto.response;

public record CreateChatThreadResponse(
        String content,
        Long userChatThreadId
) {
    public static CreateChatThreadResponse of(String content, Long userChatThreadId) {
        return new CreateChatThreadResponse(content, userChatThreadId);
    }
}
