package com.umc9th.areumdap.domain.chatbot.service;

import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendResponse;
import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendsResponse;
import com.umc9th.areumdap.domain.question.service.QuestionCommandService;
import com.umc9th.areumdap.domain.question.service.QuestionQueryService;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatbotQueryService {

    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;

    @Transactional
    public GetChatbotRecommendsResponse getRecommendQuestions(Long userId) {
        List<UserQuestion> todayQuestions = questionQueryService.getTodayQuestions(userId);

        if (todayQuestions.size() < 5) {
            todayQuestions = questionCommandService.assignRandomQuestions(userId);
        }

        List<GetChatbotRecommendResponse> responses = todayQuestions.stream()
                .map(GetChatbotRecommendResponse::from)
                .toList();

        return new GetChatbotRecommendsResponse(responses);
    }

}