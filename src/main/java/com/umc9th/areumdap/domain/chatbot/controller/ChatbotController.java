package com.umc9th.areumdap.domain.chatbot.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.dto.request.ChatSummaryRequest;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatReportRequest;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.request.SendChatMessageRequest;
import com.umc9th.areumdap.domain.chat.dto.response.ChatSummaryResponse;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatReportResponse;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import com.umc9th.areumdap.domain.chat.dto.response.SendChatMessageResponse;
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
    private final ChatCommandService chatCommandService;

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<GetChatbotRecommendsResponse>> getChatbotRecommend(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_CHATBOT_RECOMMEND_SUCCESS,
                chatbotQueryService.getRecommendQuestions(userId)
        );
    }

    @Override
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<CreateChatThreadResponse>> createChatThread(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatThreadRequest request

    ) {
        CreateChatThreadResponse response = chatCommandService.createChatThread(userId, request);
        return ApiResponse.success(SuccessStatus.CREATE_CHAT_THREAD_SUCCESS, response);
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<SendChatMessageResponse>> sendChatResponse(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody SendChatMessageRequest request
    ){
        SendChatMessageResponse response = chatCommandService.sendChatMessage(userId, request);
        return ApiResponse.success(SuccessStatus.SEND_CHAT_MESSAGE_SUCCESS, response);
    }

    @Override
    @PostMapping("/summary")
    public ResponseEntity<ApiResponse<ChatSummaryResponse>> generateSummary(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ChatSummaryRequest request //userChatThreadId
    ) {
        ChatSummaryResponse response = chatCommandService.generateSummary(userId, request);
        return ApiResponse.success(SuccessStatus.GENERATE_CHAT_SUMMARY_SUCCESS, response);
    }

    @Override
    @PostMapping("/report")
    public ResponseEntity<ApiResponse<CreateChatReportResponse>> createChatReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatReportRequest request
    ) {
        CreateChatReportResponse response = chatCommandService.createChatReport(userId, request);
        return ApiResponse.success(SuccessStatus.CREATE_CHAT_REPORT_SUCCESS, response);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteChatThread(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long userChatThreadId
    ) {
        chatCommandService.deleteChatThread(userId, userChatThreadId);
        return ApiResponse.success(SuccessStatus.DELETE_CHAT_THREAD_SUCCESS);
    }
}
