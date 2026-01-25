package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.mission.enums.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

public record CharacterQuestDto(
        @Schema(description = "퀘스트 ID", example = "1")
        Long questId,

        @Schema(description = "태그", example = "CAREER")
        Tag tag,

        @Schema(description = "퀘스트 제목", example = "자소서 1회 작성하기")
        String title,

        @Schema(description = "D-Day", example = "3")
        Integer dDay,

        @Schema(description = "완료 여부", example = "false")
        Boolean isCompleted
) {
}
