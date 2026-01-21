package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.user.enums.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record CharacterCreateResponse(
        @Schema(description = "생성된 캐릭터 ID", example = "1")
        Long characterId,

        @Schema(description = "유저 ID", example = "10")
        Long userId,

        @Schema(description = "캐릭터 계절", example = "SPRING")
        Season season
) {
}
