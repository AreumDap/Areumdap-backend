package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.character.entity.Character;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetCharacterResponse(
        @Schema(description = "캐릭터 ID", example = "1")
        Long characterId,

        @Schema(description = "유저 닉네임", example = "웰시")
        String nickname,

        @Schema(description = "캐릭터 레벨", example = "1")
        Integer level,

        @Schema(description = "현재 XP", example = "15")
        Integer currentXp,

        @Schema(description = "목표 XP", example = "100")
        Integer goalXp,

        @Schema(description = "레벨업 가능 여부", example = "false")
        Boolean hasLevelUpParams,

        @Schema(description = "미션 목록")
        List<CharacterMissionDto> missions,

        @Schema(description = "캐릭터 이미지")
        String imageUrl
) {
    public static GetCharacterResponse from(Character character, boolean canLevelUp, List<CharacterMissionDto> missions, String imageUrl) {
        return new GetCharacterResponse(
                character.getId(),
                "아름이",
                character.getLevel(),
                character.getCurrentXp(),
                character.getGoalXp(),
                canLevelUp,
                missions,
                imageUrl
        );
    }

}
