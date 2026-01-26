package com.umc9th.areumdap.domain.chat.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.controller.docs.ChatControllerDocs;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import com.umc9th.areumdap.domain.chat.service.ChatCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {
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
