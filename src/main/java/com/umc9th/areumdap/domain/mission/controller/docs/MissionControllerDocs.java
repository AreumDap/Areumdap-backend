package com.umc9th.areumdap.domain.mission.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CompleteMissionRequest;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Mission", description = "성찰과제 관련 API")
public interface MissionControllerDocs {

    @Operation(summary = "성찰과제 상세 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성찰과제 상세 조회 성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MissionResponse.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "성공",
                            summary = "성공 시 응답",
                            value = "{\"isSuccess\": true, \"code\": \"MISSION_200\", \"message\": \"성찰과제 상세 조회 성공\", \"data\": {\"missionId\": 1, \"tag\": \"RELATIONSHIP\", \"title\": \"친구에게 연락하기\", \"description\": \"오랫동안 연락하지 않은 친구에게 연락해보세요.\", \"guide\": \"가벼운 안부 인사로 시작해보세요.\", \"rewardXp\": 30, \"dDay\": 5, \"status\": \"ASSIGNED\"}}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 과제", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "과제 접근 권한 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<MissionResponse>> getMissionDetail(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "과제 ID", required = true) @PathVariable(name = "missionId") Long missionId
    );

    @Operation(summary = "과제 수행 완료")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "과제 수행 완료 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "성공",
                            summary = "성공 시 응답",
                            value = "{\"isSuccess\": true, \"code\": \"MISSION_201\", \"message\": \"과제 수행 완료! XP가 지급되었습니다.\", \"data\": null}"
                    ))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 과제", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "단계 업그레이드 필요", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 완료한 과제", content = @Content())
    })
    @PostMapping("/complete")
    ResponseEntity<ApiResponse<Void>> completeMission(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CompleteMissionRequest request
    );
}