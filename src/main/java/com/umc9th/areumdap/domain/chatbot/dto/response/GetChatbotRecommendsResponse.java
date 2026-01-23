package com.umc9th.areumdap.domain.chatbot.dto.response;

import java.util.List;

public record GetChatbotRecommendsResponse(
        List<GetChatbotRecommendResponse> questions
) {
}
