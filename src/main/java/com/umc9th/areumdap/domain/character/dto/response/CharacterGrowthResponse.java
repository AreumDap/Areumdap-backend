package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
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
}
