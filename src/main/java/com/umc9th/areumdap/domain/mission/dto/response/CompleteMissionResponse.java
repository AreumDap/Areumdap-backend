package com.umc9th.areumdap.domain.mission.dto.response;

public record CompleteMissionResponse(
        Long missionId
) {
    public static CompleteMissionResponse from(Long missionId) {
        return new CompleteMissionResponse(missionId);
    }
}
