package com.umc9th.areumdap.domain.chat.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatReportRequest;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatControllerDocs {

    @PostMapping("/thread")
    @Operation(summary = "채팅방 생성", description = "새로운 채팅 스레드를 생성하고 첫 질문을 저장합니다. userQuestionId가 null인 경우 랜덤 인사 메시지로 대화를 시작합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "채팅 스레드 생성 성공",  content = @Content(schema = @Schema(implementation = CreateChatThreadResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<CreateChatThreadResponse>> createChatThread(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatThreadRequest request
    );

    @Operation(
            summary = "채팅방 목록 조회",
            description = """
                 유저의 채팅방 목록을 조회합니다.
                
                - 기본 정렬: 최신순 (updatedAt DESC)
                - favorite=true: 즐겨찾기 채팅방만 최신순으로 조회
                - 무한 스크롤을 위한 커서 기반 페이징을 사용합니다.
                
                 커서 규칙
                - 첫 페이지: cursorTime, cursorId 없이 요청
                - 다음 페이지: 이전 응답의 nextCursorTime, nextCursorId 전달
                """
    )
    @GetMapping("/threads")
    ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getUserChatThreads(
            @AuthenticationPrincipal Long userId,
            @Parameter(
                    description = "true면 즐겨찾기 채팅방만 조회, false면 전체 최신순 조회"
            )
            @RequestParam(defaultValue = "false") boolean favorite,
            @Parameter(
                    description = """
                        커서 기반 페이징 요청 정보
                        
                        - cursorTime: 이전 페이지 마지막 채팅의 updatedAt
                        - cursorId: 이전 페이지 마지막 채팅의 ID
                        - size: 한 번에 가져올 채팅방 개수
                        
                         cursorTime과 cursorId는 반드시 함께 전달되어야 합니다.
                        """
            )
            @Valid @ModelAttribute UserChatThreadCursorRequest cursor
    );

    @PostMapping("/report")
    @Operation(summary = "채팅 레포트 생성", description = "대화 요약을 기반으로 채팅 레포트를 생성합니다. 먼저 /api/chatbot/summary로 요약을 생성한 후 호출해야 합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "채팅 레포트 생성 성공", content = @Content(schema = @Schema(implementation = CreateChatReportResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "대화 요약이 존재하지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "채팅 스레드 접근 권한 없음", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "채팅 스레드를 찾을 수 없음", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 레포트가 존재하는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<CreateChatReportResponse>> createChatReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateChatReportRequest request
    );

    @GetMapping("/reports/{reportId}")
    @Operation(summary = "채팅 레포트 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채팅 레포트 조회 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않은 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "레포트가 존재하지 않은 경우", content = @Content()),
    })
    ResponseEntity<ApiResponse<GetChatReportResponse>> getChatReport(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long reportId
    );

    @GetMapping("/threads/{threadId}")
    @Operation(summary = "채팅 메세지 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채팅 기록 조회 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않은 경우", content = @Content()),
    })
    ResponseEntity<ApiResponse<GetChatHistoriesResponse>> getChatHistories(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long threadId
    );

    @DeleteMapping
    @Operation(summary = "채팅 스레드 삭제", description = "채팅 스레드와 관련 데이터를 삭제합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "대화 스레드 삭제 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "채팅 스레드 접근 권한 없음", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "채팅 스레드를 찾을 수 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> deleteChatThread(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long userChatThreadId
    );

    @PatchMapping("/threads/{threadId}/favorite")
    @Operation(summary = "채팅방 즐겨찾기 설정/해제")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "즐겨찾기 상태 변경 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않은 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "채팅방이 존재하지 않는 경우", content = @Content()),
    })
    ResponseEntity<ApiResponse<Void>> updateFavorite(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long threadId
    );
}
