package com.umc9th.areumdap.domain.mission.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.response.CompleteMissionResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

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
                            value = "{\"isSuccess\": true, \"code\": \"MISSION_200\", \"message\": \"성찰과제 상세 조회 성공\", \"data\": {\"missionId\": 1, \"category\": \"RELATIONSHIP\", \"title\": \"친구에게 연락하기\", \"description\": \"오랫동안 연락하지 않은 친구에게 연락해보세요.\", \"guide\": \"가벼운 안부 인사로 시작해보세요.\", \"rewardXp\": 30, \"dDay\": 5, \"status\": \"ASSIGNED\"}}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 과제", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "과제 없음",
                            summary = "존재하지 않는 과제 조회 시",
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_404\", \"message\": \"존재하지 않는 과제입니다.\", \"data\": null}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "과제 접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "권한 없음",
                            summary = "다른 사용자의 과제 조회 시",
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_403\", \"message\": \"해당 성찰과제에 접근할 권한이 없습니다.\", \"data\": null}"
                    )
            ))
    })
    ResponseEntity<ApiResponse<MissionResponse>> getMissionDetail(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "과제 ID", required = true) @PathVariable(name = "missionId") Long missionId
    );

    @Operation(summary = "과제 수행 완료")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "과제 수행 완료 성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CompleteMissionResponse.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "성공",
                            summary = "성공 시 응답",
                            value = "{\"isSuccess\": true, \"code\": \"MISSION_201\", \"message\": \"과제 수행 완료! XP가 지급되었습니다.\", \"data\": {\"missionId\": 1}}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 과제", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "과제 없음",
                            summary = "존재하지 않는 과제 완료 시",
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_404\", \"message\": \"존재하지 않는 과제입니다.\", \"data\": null}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 완료한 과제", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "이미 완료됨",
                            summary = "이미 완료된 과제 완료 시도 시",
                            value = "{\"isSuccess\": false, \"code\": \"MISSION_409\", \"message\": \"이미 완료한 과제입니다.\", \"data\": null}"
                    )
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "단계 업그레이드 필요", content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "레벨업 필요",
                            summary = "XP가 충분하여 레벨업이 필요한 경우",
                            value = "{\"isSuccess\": false, \"code\": \"CHAR_400\", \"message\": \"단계 업그레이드가 필요합니다.\", \"data\": null}"
                    )
            ))
    })
    ResponseEntity<ApiResponse<CompleteMissionResponse>> completeMission(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "과제 ID", required = true) @PathVariable(name = "missionId") Long missionId
    );
}