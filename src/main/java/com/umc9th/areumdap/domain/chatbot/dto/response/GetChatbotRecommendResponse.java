package com.umc9th.areumdap.domain.chatbot.dto.response;

public record GetChatbotRecommendResponse(
        Long questionBankId,
        String content,
        String tag
) {
}
