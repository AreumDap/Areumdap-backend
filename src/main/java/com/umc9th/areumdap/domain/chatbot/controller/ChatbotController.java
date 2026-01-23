package com.umc9th.areumdap.domain.chatbot.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chatbot.controller.docs.ChatbotControllerDocs;
import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendResponse;
import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendsResponse;
import com.umc9th.areumdap.domain.question.entity.UserQuestion;
import com.umc9th.areumdap.domain.question.service.QuestionCommandService;
import com.umc9th.areumdap.domain.question.service.QuestionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/chatbot")
@RestController
@RequiredArgsConstructor
public class ChatbotController implements ChatbotControllerDocs {
    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<GetChatbotRecommendsResponse>> getChatbotRecommend(
            @AuthenticationPrincipal Long userId
    ) {
        List<UserQuestion> todayQuestions = questionQueryService.getTodayQuestions(userId);
        if(todayQuestions.size() < 5){
            todayQuestions = questionCommandService.assignRandomQuestions(userId);
        }

        List<GetChatbotRecommendResponse> responses = todayQuestions.stream()
                .map(uq -> new GetChatbotRecommendResponse(
                        uq.getQuestionBank().getId(),
                        uq.getQuestionBank().getContent(),
                        uq.getQuestionBank().getTag().name()
                ))
                .toList();
        return ApiResponse.success(
                SuccessStatus.GET_CHATBOT_RECOMMAND_SUCCESS,
                new GetChatbotRecommendsResponse(responses)
        );
    }
}
