package com.umc9th.areumdap.domain.chatbot.dto.response;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;

public record GetChatbotRecommendResponse(
        Long userQuestionId,
        String content,
        String tag
) {
    public static GetChatbotRecommendResponse from(UserQuestion userQuestion) {
        return new GetChatbotRecommendResponse(
                userQuestion.getId(),
                userQuestion.getQuestionBank().getContent(),
                userQuestion.getQuestionBank().getTag().name()
        );
    }
}
