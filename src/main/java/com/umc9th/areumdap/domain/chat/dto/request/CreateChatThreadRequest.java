package com.umc9th.areumdap.domain.chat.dto.request;

public record CreateChatThreadRequest(
        String content,
        Long userQuestionId
) {
}
