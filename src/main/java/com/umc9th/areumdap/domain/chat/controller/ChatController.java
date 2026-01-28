package com.umc9th.areumdap.domain.chat.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.controller.docs.ChatControllerDocs;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatReportResponse;
import com.umc9th.areumdap.domain.chat.service.ChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {

    private final ChatQueryService chatQueryService;

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
