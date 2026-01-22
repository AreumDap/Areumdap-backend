package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record MissionResponse(
        Long missionId,
        String title,
        MissionStatus status,
        Tag tag,
        Long reward,
        LocalDateTime completedAt // 완료일자
) {
    public static MissionResponse from(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getTitle(),
                mission.getMissionStatus(),
                mission.getTag(),
                mission.getReward(),
                mission.getUpdatedAt()
        );
    }

    public static List<MissionResponse> from(List<Mission> missions) {
        return missions.stream()
                .map(MissionResponse::from)
                .toList();
    }
}
