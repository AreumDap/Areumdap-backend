package com.umc9th.areumdap.domain.mission.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record CursorResponse(
        List<MissionResponse> missions,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static CursorResponse empty() {
        return new CursorResponse(
                List.of(),
                null,
                null,
                false
        );
    }
}
