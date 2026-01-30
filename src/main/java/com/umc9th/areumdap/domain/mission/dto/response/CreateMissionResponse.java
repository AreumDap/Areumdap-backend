package com.umc9th.areumdap.domain.mission.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record CreateMissionResponse(
        Long userChatThreadId,
        List<MissionDto> missions
) {
    public record MissionDto(
            Long missionId,
            Tag tag,
            String title,
            String content,
            String tip,
            LocalDateTime dueDate,
            Long reward,
            MissionStatus status
    ) {
        public static MissionDto from(Mission mission) {
            return new MissionDto(
                    mission.getId(),
                    mission.getTag(),
                    mission.getTitle(),
                    mission.getContent(),
                    mission.getTip(),
                    mission.getDueDate(),
                    mission.getReward(),
                    mission.getMissionStatus()
            );
        }
    }

    public static CreateMissionResponse of(Long userChatThreadId, List<Mission> missions) {
        List<MissionDto> missionDtos = missions.stream()
                .map(MissionDto::from)
                .toList();
        return new CreateMissionResponse(userChatThreadId, missionDtos);
    }
}
