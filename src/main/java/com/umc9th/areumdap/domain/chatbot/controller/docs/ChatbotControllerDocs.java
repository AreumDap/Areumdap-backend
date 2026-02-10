package com.umc9th.areumdap.domain.chatbot.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.ChatSummaryRequest;
import com.umc9th.areumdap.domain.chat.dto.request.SendChatMessageRequest;
import com.umc9th.areumdap.domain.chat.dto.response.ChatSummaryResponse;
import com.umc9th.areumdap.domain.chat.dto.response.SendChatMessageResponse;
import com.umc9th.areumdap.domain.chatbot.dto.response.GetChatbotRecommendsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@Tag(name = "Chatbot", description = "챗봇 API")
public interface ChatbotControllerDocs {

    @GetMapping("/recommend")
    @Operation(summary = "오늘의 추천 질문 조회", description = "매일 5개의 랜덤 질문을 조회합니다. 당일 배정된 질문이 없으면 새로 배정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 질문 조회 성공", content = @Content(schema = @Schema(implementation = GetChatbotRecommendsResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "배정 가능한 질문이 부족한 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<GetChatbotRecommendsResponse>> getChatbotRecommend(
            @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "채팅 메시지 생성", description = "사용자 메시지를 전송하고 AI 응답을 받습니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메시지 전송 성공", content = @Content(schema = @Schema(implementation = SendChatMessageResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "채팅 스레드 접근 권한 없음", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 또는 채팅 스레드를 찾을 수 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<SendChatMessageResponse>> sendChatResponse(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody SendChatMessageRequest request
    );

    @Operation(summary = "대화 요약 생성", description = "대화 스레드의 전체 대화 내역을 기반으로 AI 요약을 생성합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "대화 요약 생성 성공", content = @Content(schema = @Schema(implementation = ChatSummaryResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "채팅 스레드 접근 권한 없음", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "채팅 스레드를 찾을 수 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<ChatSummaryResponse>> generateSummary(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ChatSummaryRequest request
    );

}
