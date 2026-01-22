package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.umc9th.areumdap.domain.character.entity.Character;

public record CharacterGrowthResponse(
        @Schema(description = "캐릭터 ID", example = "1")
        Long characterId,

        @Schema(description = "기존 레벨", example = "1")
        Integer previousLevel,

        @Schema(description = "성장 후 레벨", example = "2")
        Integer currentLevel,

        @Schema(description = "성장 메시지", example = "아름이가 2단계로 성장했어요!")
        String growthMessage,

        @Schema(description = "다음 성장 안내 메시지", example = "다음 성장을 위해선 100 XP가 필요해요.")
        String nextLevelGuide
) {
        public static CharacterGrowthResponse from(Character character, Integer previousLevel) {
                String characterName = "아름이";
                String growthMessage = String.format("%s가 %d단계로 성장했어요!", characterName, character.getLevel());

                String nextLevelGuide;
                if (character.isMaxLevel()) {
                        nextLevelGuide = "최고 레벨입니다.";
                } else {
                        int remainingXp = Math.max(0, character.getGoalXp() - character.getCurrentXp());
                        nextLevelGuide = String.format("다음 성장을 위해선 %d XP가 필요해요.", remainingXp);
                }

                return new CharacterGrowthResponse(
                        character.getId(),
                        previousLevel,
                        character.getLevel(),
                        growthMessage,
                        nextLevelGuide
                );
        }
}
