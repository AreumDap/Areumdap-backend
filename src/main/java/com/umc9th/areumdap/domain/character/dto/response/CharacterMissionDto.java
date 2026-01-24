package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record CharacterMissionDto(
        @Schema(description = "미션 ID", example = "1")
        Long missionId,

        @Schema(description = "미션 태그", example = "CAREER")
        Tag tag,

        @Schema(description = "미션 제목", example = "자소서 1회 작성하기")
        String title,

        @Schema(description = "D-Day", example = "3")
        Integer dDay,

        @Schema(description = "완료 여부", example = "false")
        Boolean isCompleted
) {
    public static CharacterMissionDto from(Mission mission) {
        Objects.requireNonNull(mission, "mission must not be null");
        return new CharacterMissionDto(
                mission.getId(),
                mission.getTag(),
                mission.getTitle(),
                (int) ChronoUnit.DAYS.between(java.time.LocalDate.now(), mission.getDueDate().toLocalDate()),
                mission.getMissionStatus() == MissionStatus.COMPLETED
        );
    }
}
