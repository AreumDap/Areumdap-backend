package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.temporal.ChronoUnit;

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
        return new MissionResponse(
                mission.getId(),
                mission.getTag(),
                mission.getTitle(),
                mission.getContent(),
                mission.getTip(),
                mission.getReward(),
                (int) ChronoUnit.DAYS.between(java.time.LocalDateTime.now(), mission.getDueDate()),
                mission.getMissionStatus()
        );
    }
}
