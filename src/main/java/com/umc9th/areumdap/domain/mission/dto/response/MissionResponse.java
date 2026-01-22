package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import lombok.Builder;

import java.time.temporal.ChronoUnit;

@Builder
public record MissionResponse(
        Long missionId,
        Tag category,
        String title,
        String description,
        String guide,
        Long rewardXp,
        Integer dDay,

        MissionStatus status
) {
    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
                .missionId(mission.getId())
                .category(mission.getTag())
                .title(mission.getTitle())
                .description(mission.getContent())
                .guide(mission.getTip())
                .rewardXp(mission.getReward())
                .dDay((int) ChronoUnit.DAYS.between(java.time.LocalDateTime.now(), mission.getDueDate()))
                .status(mission.getMissionStatus())
                .build();
    }
}
