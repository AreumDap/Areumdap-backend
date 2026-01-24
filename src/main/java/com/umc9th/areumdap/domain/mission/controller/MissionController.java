package com.umc9th.areumdap.domain.mission.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.controller.docs.MissionControllerDocs;
import com.umc9th.areumdap.domain.mission.dto.request.CompleteMissionRequest;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.service.MissionCommandService;
import com.umc9th.areumdap.domain.mission.service.MissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController implements MissionControllerDocs {

    private final MissionCommandService missionCommandService;
    private final MissionQueryService missionQueryService;

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
}
