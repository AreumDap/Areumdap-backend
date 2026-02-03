package com.umc9th.areumdap.domain.mission.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MissionCursorResponse(
        long totalCount,                 // tag 기준 전체 개수
        List<GetMissionResponse> missions,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static MissionCursorResponse of(
            long totalCount,
            List<GetMissionResponse> missions,
            LocalDateTime nextCursorTime,
            Long nextCursorId,
            boolean hasNext
    ) {
        return new MissionCursorResponse(
                totalCount,
                missions,
                nextCursorTime,
                nextCursorId,
                hasNext
        );
    }

    public static MissionCursorResponse empty(long totalCount) {
        return new MissionCursorResponse(
                totalCount,
                List.of(),
                null,
                null,
                false
        );
    }
}
