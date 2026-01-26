package com.umc9th.areumdap.domain.chat.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getThreads(
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
            @ModelAttribute UserChatThreadCursorRequest cursor
    );
}
