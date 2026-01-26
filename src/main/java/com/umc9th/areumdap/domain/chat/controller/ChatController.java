package com.umc9th.areumdap.domain.chat.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.controller.docs.ChatControllerDocs;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import com.umc9th.areumdap.domain.chat.service.UserChatThreadQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/chat")
@RestController
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {

    private final UserChatThreadQueryService service;

    @Override
    @GetMapping("/threads")
    public ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getThreads(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "false") boolean favorite,
            @ModelAttribute UserChatThreadCursorRequest cursor
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_USER_CHAT_THREADS_SUCCESS,
                service.getThreads(userId, favorite, cursor)
        );
    }

}
