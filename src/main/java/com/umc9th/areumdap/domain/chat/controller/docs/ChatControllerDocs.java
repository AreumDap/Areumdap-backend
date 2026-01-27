package com.umc9th.areumdap.domain.chat.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatControllerDocs {

    @GetMapping("/reports/{reportId}")
    @Operation(summary = "채팅 레포트 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채팅 레포트 조회 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "레포트가 존재하지 않는 경우", content = @Content()),
    })
    ResponseEntity<ApiResponse<GetChatReportResponse>> getChatReport(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long reportId
    );

}
