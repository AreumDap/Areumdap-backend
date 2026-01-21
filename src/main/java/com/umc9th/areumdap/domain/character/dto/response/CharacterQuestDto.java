package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.umc9th.areumdap.domain.character.enums.QuestCategory;
import lombok.Builder;

@Builder
public record CharacterQuestDto(
        @Schema(description = "퀘스트 ID", example = "1")
        Long questId,

        @Schema(description = "퀘스트 카테고리", example = "CAREER")
        QuestCategory category,

        @Schema(description = "퀘스트 제목", example = "자소서 1회 작성하기")
        String title,

        @Schema(description = "D-Day", example = "3")
        Integer dDay,

        @Schema(description = "완료 여부", example = "false")
        Boolean isCompleted
) {
    public static CharacterQuestDto of(Long questId, QuestCategory category, String title, Integer dDay, Boolean isCompleted) {
        return new CharacterQuestDto(questId, category, title, dDay, isCompleted);
    }
}
