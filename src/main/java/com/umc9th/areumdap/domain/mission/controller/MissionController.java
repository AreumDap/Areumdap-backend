package com.umc9th.areumdap.domain.mission.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.controller.docs.MissionControllerDocs;
import com.umc9th.areumdap.domain.mission.dto.response.MissionCursorResponse;
import com.umc9th.areumdap.domain.mission.service.MissionQueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController implements MissionControllerDocs {

    private final MissionQueryService missionQueryService;

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
}
