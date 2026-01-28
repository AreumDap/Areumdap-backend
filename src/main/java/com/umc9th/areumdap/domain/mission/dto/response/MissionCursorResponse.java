package com.umc9th.areumdap.domain.mission.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MissionCursorResponse(
        List<GetMissionResponse> missions,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static MissionCursorResponse empty() {
        return new MissionCursorResponse(
                List.of(),
                null,
                null,
                false
        );
    }
}
