package com.umc9th.areumdap.domain.mission.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.controller.docs.MissionControllerDocs;
import com.umc9th.areumdap.domain.mission.dto.response.CursorResponse;
import com.umc9th.areumdap.domain.mission.service.MissionQueryService;

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
    public ResponseEntity<ApiResponse<CursorResponse>> getCompletedMissions(
            @AuthenticationPrincipal Long userId,
            CursorRequest request
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_All_COMPLETED_MISSION_SUCCESS,
                missionQueryService.getCompletedMissionsWithCursor(
                        userId, request
                )
        );
    }
}
