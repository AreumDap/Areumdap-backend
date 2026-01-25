package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record GetMissionResponse(
        Long missionId,
        String title,
        MissionStatus status,
        Tag tag,
        Long reward,
        LocalDateTime completedAt // 완료일자
) {
    public static GetMissionResponse from(Mission mission) {
        return new GetMissionResponse(
                mission.getId(),
                mission.getTitle(),
                mission.getMissionStatus(),
                mission.getTag(),
                mission.getReward(),
                mission.getCompletedAt()
        );
    }

    public static List<GetMissionResponse> from(List<Mission> missions) {
        return missions.stream()
                .map(GetMissionResponse::from)
                .toList();
    }
}