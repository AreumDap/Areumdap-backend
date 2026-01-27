package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;

public record MissionSummaryResponse(
        Long missionId,
        Tag tag,
        String title,
        String content,
        MissionStatus status
) {
}

