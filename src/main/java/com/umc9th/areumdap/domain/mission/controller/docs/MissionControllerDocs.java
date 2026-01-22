package com.umc9th.areumdap.domain.mission.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.dto.response.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Mission", description = "성찰과제 API")
public interface MissionControllerDocs {

    @GetMapping("/completed")
    @Operation(summary = "태그별 완료된 과제 조회",
            description = """
                    태그별로 완료된 과제를 조회합니다.
                    아무 태그 입력하지 않을 시 완료된 모든 과제를 불러옵니다.
                    """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "완료된 과제 조회 성공", content = @Content(schema = @Schema(implementation = CursorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 과제 요청", content = @Content())
    })
    ResponseEntity<ApiResponse<CursorResponse>> getCompletedMissions(
            @AuthenticationPrincipal Long userId,
            CursorRequest request
    );
}
