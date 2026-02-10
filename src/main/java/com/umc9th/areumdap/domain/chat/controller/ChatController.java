package com.umc9th.areumdap.domain.chat.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.controller.docs.ChatControllerDocs;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatReportRequest;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.*;
import com.umc9th.areumdap.domain.chat.service.ChatCommandService;
import com.umc9th.areumdap.domain.chat.service.ChatQueryService;
import com.umc9th.areumdap.domain.chat.service.UserChatThreadQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {

    private final ChatQueryService chatQueryService;
    private final ChatCommandService chatCommandService;
    private final UserChatThreadQueryService userChatThreadQueryService;

    @Override
    @PostMapping("/thread")
    public ResponseEntity<ApiResponse<CreateChatThreadResponse>> createChatThread(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatThreadRequest request

    ) {
        CreateChatThreadResponse response = chatCommandService.createChatThread(userId, request);
        return ApiResponse.success(SuccessStatus.CREATE_CHAT_THREAD_SUCCESS, response);
    }

    @Override
    @GetMapping("/threads")
    public ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getUserChatThreads(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "false") boolean favorite,
            @Valid @ModelAttribute UserChatThreadCursorRequest cursor
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_USER_CHAT_THREADS_SUCCESS,
                userChatThreadQueryService.getThreads(userId, favorite, cursor)
        );
    }

    @Override
    @GetMapping("/threads/{threadId}")
    public ResponseEntity<ApiResponse<GetChatHistoriesResponse>> getChatHistories(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long threadId
    ){
        return ApiResponse.success(
                SuccessStatus.GET_CHAT_HISTORIES_SUCCESS,
                chatQueryService.getChatHistories(userId,threadId)
        );
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
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<ApiResponse<GetChatReportResponse>> getChatReport(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long reportId
    ) {

        return ApiResponse.success(
                SuccessStatus.GET_CHAT_REPORT_SUCCESS,
                chatQueryService.getChatReport(userId, reportId)
        );
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

    @Override
    @PatchMapping("/threads/{threadId}/favorite")
    public ResponseEntity<ApiResponse<Void>> updateFavorite(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long threadId
    ) {
        chatCommandService.updateFavorite(userId, threadId);
        return ApiResponse.success(SuccessStatus.UPDATE_FAVORITE_SUCCESS);
    }
}
