package com.umc9th.areumdap.domain.chat.dto.response;

public record SendChatMessageResponse(
        String content,
        Long userChatThreadId
) {}
