package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record CharacterMeResponse(
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
        List<CharacterMissionDto> missions
) {
}
