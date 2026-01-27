package com.umc9th.areumdap.domain.character.dto.response;

import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

public record CharacterHistoryDto(
        @Schema(description = "달성 레벨", example = "1")
        Integer level,

        @Schema(description = "달성 날짜", example = "2026-01-14")
        LocalDate achievedDate,

        @Schema(description = "캐릭터 이미지")
                String imageUrl
) {
        public static CharacterHistoryDto of(CharacterHistory characterHistory, String imageUrl) {
                return new CharacterHistoryDto(
                        characterHistory.getLevel(),
                        characterHistory.getCreatedAt().toLocalDate(),
                        imageUrl
                );
        }
}