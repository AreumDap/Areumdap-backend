package com.umc9th.areumdap.domain.mission.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CreateMissionRequest;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.controller.docs.MissionControllerDocs;
import com.umc9th.areumdap.domain.mission.dto.response.CreateMissionResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CompleteMissionRequest;
import com.umc9th.areumdap.domain.mission.service.MissionCommandService;
import com.umc9th.areumdap.domain.mission.dto.response.MissionCursorResponse;
import com.umc9th.areumdap.domain.mission.service.MissionQueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController implements MissionControllerDocs {

    private final MissionCommandService missionCommandService;
    private final MissionQueryService missionQueryService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<CreateMissionResponse>> createMissions(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateMissionRequest request
    ) {
        CreateMissionResponse response = missionCommandService.createMissions(userId, request.userChatThreadId());
        return ApiResponse.success(SuccessStatus.CREATE_MISSION_SUCCESS, response);
    }

    @Override
    @GetMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> getMissionDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "missionId") Long missionId
    ) {
        MissionResponse response = missionQueryService.getMissionDetail(missionId, userId);
        return ApiResponse.success(SuccessStatus.GET_MISSION_DETAIL_SUCCESS, response);
    }

    @Override
    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<Void>> completeMission(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CompleteMissionRequest request
    ) {
        missionCommandService.completeMission(userId, request.missionId());
        return ApiResponse.success(SuccessStatus.COMPLETE_MISSION_SUCCESS);
    }

    @Override
    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<MissionCursorResponse>> getCompletedMissions(
            @AuthenticationPrincipal Long userId,
            @Valid @ModelAttribute CursorRequest request
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_ALL_COMPLETED_MISSION_SUCCESS,
                missionQueryService.getCompletedMissionsWithCursor(
                        userId, request
                )
        );
    }

    @Override
    @DeleteMapping("/{missionId}")
    public ResponseEntity<ApiResponse<Void>> deleteMission(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "missionId") Long missionId
    ) {
        missionCommandService.deleteMission(userId, missionId);
        return ApiResponse.success(SuccessStatus.DELETE_MISSION_SUCCESS);
    }

}
