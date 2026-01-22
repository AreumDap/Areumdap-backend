package com.umc9th.areumdap.domain.mission.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.controller.docs.MissionControllerDocs;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.service.MissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController implements MissionControllerDocs {

    private final MissionQueryService missionQueryService;

    @Override
    @GetMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> getMissionDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long missionId
    ) {
        MissionResponse missionResponse = missionQueryService.getMissionDetail(missionId, userId);
        
        return ApiResponse.success(SuccessStatus.GET_MISSION_DETAIL_SUCCESS, missionResponse);
    }
}
