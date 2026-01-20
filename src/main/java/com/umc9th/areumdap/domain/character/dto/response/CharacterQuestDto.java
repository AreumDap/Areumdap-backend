package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.character.enums.QuestCategory;
import lombok.Builder;

@Builder
public record CharacterQuestDto(
        Long questId,
        QuestCategory category,
        String title,
        Integer dDay,
        Boolean isCompleted
) {
    public static CharacterQuestDto of(Long questId, QuestCategory category, String title, Integer dDay, Boolean isCompleted) {
        return new CharacterQuestDto(questId, category, title, dDay, isCompleted);
    }
}
