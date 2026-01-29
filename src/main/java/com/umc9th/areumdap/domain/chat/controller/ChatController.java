package com.umc9th.areumdap.domain.chat.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.controller.docs.ChatControllerDocs;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatHistoriesResponse;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatReportResponse;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
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
    private final UserChatThreadQueryService userChatThreadQueryService;

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

}
