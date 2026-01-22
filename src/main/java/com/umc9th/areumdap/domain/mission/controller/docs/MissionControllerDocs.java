package com.umc9th.areumdap.domain.mission.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Mission", description = "성찰과제 API")
public interface MissionControllerDocs {

    @GetMapping("/{missionId}")
    @Operation(summary = "성찰과제 상세 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "과제 상세 조회 성공", content = @Content(schema = @Schema(implementation = MissionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 과제 ID", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_404\", \"message\": \"존재하지 않는 과제입니다.\"}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "다른 사람의 과제 ID", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_403\", \"message\": \"해당 과제에 접근할 권한이 없습니다.\"}"
                    )
            ))
    })
    ResponseEntity<ApiResponse<MissionResponse>> getMissionDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long missionId
    );
}
