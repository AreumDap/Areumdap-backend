package com.umc9th.areumdap.domain.mission.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CompleteMissionRequest;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.dto.response.MissionCursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Mission", description = "성찰과제 API")
public interface MissionControllerDocs {

    @PostMapping("/complete")
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

    ResponseEntity<ApiResponse<Void>> completeMission(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CompleteMissionRequest request
    );

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

    @GetMapping("/completed")
    @Operation(summary = "태그별 완료된 과제 조회",
            description = """
                    커서 기반 무한 스크롤 방식으로 태그별 과제 목록을 조회합니다.
                    아무 태그 입력하지 않을 시 완료된 모든 과제를 불러옵니다.
                    - 첫 요청: cursorTime, cursorId 없이 호출
                    - 다음 요청: 이전 응답의 nextCursorTime, nextCursorId를 그대로 전달
                    """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "완료된 과제 조회 성공", content = @Content(schema = @Schema(implementation = MissionCursorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 과제 요청", content = @Content())
    })
    ResponseEntity<ApiResponse<MissionCursorResponse>> getCompletedMissions(
            @AuthenticationPrincipal Long userId,
            @Valid @ModelAttribute CursorRequest request
    );

}