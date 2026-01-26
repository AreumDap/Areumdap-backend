package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterCharacterResponse(
        @Schema(description = "생성된 캐릭터 ID", example = "1")
        Long characterId,

        @Schema(description = "생성된 캐릭터 이미지", example = "https://~~~")
        String imageUrl
) {
}
