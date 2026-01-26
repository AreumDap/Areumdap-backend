package com.umc9th.areumdap.domain.chat.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatControllerDocs {

    @Operation(summary = "채팅 스레드 생성", description = "새로운 채팅 스레드를 생성하고 첫 질문을 저장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "채팅 스레드 생성 성공",  content = @Content(schema = @Schema(implementation = CreateChatThreadResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 또는 유저 질문을 찾을 수 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<CreateChatThreadResponse>> createChatThread(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateChatThreadRequest request
    );
}
