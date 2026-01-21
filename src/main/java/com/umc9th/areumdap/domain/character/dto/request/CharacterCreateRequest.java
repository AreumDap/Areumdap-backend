package com.umc9th.areumdap.domain.character.dto.request;

import com.umc9th.areumdap.domain.user.enums.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CharacterCreateRequest(
        @Schema(description = "선택한 계절", example = "SPRING")
        @NotNull(message = "계절은 필수 입력 값입니다.")
        Season season,

        @Schema(description = "선택한 키워드", example = "[\"WARM\", \"BRIGHT\"]")
        @NotNull(message = "키워드는 필수 입력 값입니다.")
        @Size(max = 3, message = "키워드는 최대 3개까지 선택 가능합니다.")
        List<@Size(max = 100, message = "키워드는 100자를 초과할 수 없습니다.") String> keywords
) {
}
