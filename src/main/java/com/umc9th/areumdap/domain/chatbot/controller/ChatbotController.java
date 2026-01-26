package com.umc9th.areumdap.domain.chatbot.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import com.umc9th.areumdap.domain.chat.service.ChatCommandService;
import com.umc9th.areumdap.domain.chatbot.controller.docs.ChatbotControllerDocs;
import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendsResponse;
import com.umc9th.areumdap.domain.chatbot.service.ChatbotQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/chatbot")
@RestController
@RequiredArgsConstructor
public class ChatbotController implements ChatbotControllerDocs {

    private final ChatbotQueryService chatbotQueryService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<GetChatbotRecommendsResponse>> getChatbotRecommend(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_CHATBOT_RECOMMEND_SUCCESS,
                chatbotQueryService.getRecommendQuestions(userId)
        );
    }

    private final ChatCommandService chatCommandService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<CreateChatThreadResponse>> createChatThread(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatThreadRequest request
    ) {
        return ApiResponse.success(
                SuccessStatus.CREATE_CHAT_THREAD_SUCCESS,
                chatCommandService.createChatThread(userId, request)
        );
    }
}
