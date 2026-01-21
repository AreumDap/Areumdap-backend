package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.user.enums.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record CharacterCreateResponse(
        @Schema(description = "생성된 캐릭터 ID", example = "1")
        Long characterId
) {
}
