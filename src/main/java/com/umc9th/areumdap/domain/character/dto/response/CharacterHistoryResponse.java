package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record CharacterHistoryResponse(
        @Schema(description = "과거의 나", example = "과거의 나는 이러한 고민을 가지고 있었어요.")
        String pastDescription,

        @Schema(description = "현재의 나", example = "나는 이렇게 성장했어요.")
        String presentDescription,

        @Schema(description = "성장 히스토리 목록")
        List<CharacterHistoryDto> historyList
) {
}
