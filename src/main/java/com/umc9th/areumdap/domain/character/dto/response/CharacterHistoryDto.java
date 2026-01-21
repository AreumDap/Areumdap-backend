package com.umc9th.areumdap.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CharacterHistoryDto(
        @Schema(description = "달성 레벨", example = "1")
        Integer level,

        @Schema(description = "달성 날짜", example = "2026-01-14")
        LocalDate achievedDate
) {
    public static CharacterHistoryDto of(Integer level, LocalDate achievedDate) {
        return new CharacterHistoryDto(level, achievedDate);
    }
}
