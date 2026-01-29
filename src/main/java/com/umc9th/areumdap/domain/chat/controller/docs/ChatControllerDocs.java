package com.umc9th.areumdap.domain.chat.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatHistoriesResponse;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatReportResponse;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatControllerDocs {

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
    @Operation(summary = "채팅 기록 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채팅 기록 조회 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않은 경우", content = @Content()),
    })
    ResponseEntity<ApiResponse<GetChatHistoriesResponse>> getChatHistories(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long threadId
    );
}
