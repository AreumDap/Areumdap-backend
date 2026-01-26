package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.umc9th.areumdap.domain.character.entity.Character;

public record GetCharacterGrowthResponse(
        @Schema(description = "캐릭터 ID", example = "1")
        Long characterId,

        @Schema(description = "캐릭터 이름", example = "아름이")
        String characterName,

        @Schema(description = "기존 레벨", example = "1")
        Integer previousLevel,

        @Schema(description = "성장 후 레벨", example = "2")
        Integer currentLevel,

        @Schema(description = "현재 XP", example = "120")
        int currentXp,

        @Schema(description = "다음 레벨까지 필요한 XP", example = "80")
        int requiredXpForNextLevel
) {
    public static GetCharacterGrowthResponse of(Long characterId,Integer previousLevel, Integer currentLevel, int currentXp, int requiredXpForNextLevel) {
        return new GetCharacterGrowthResponse(
                characterId,
                "아름이",
                previousLevel,
                currentLevel,
                currentXp,
                requiredXpForNextLevel
        );
    }

}
