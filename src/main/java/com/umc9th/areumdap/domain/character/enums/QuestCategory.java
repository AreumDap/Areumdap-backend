package com.umc9th.areumdap.domain.character.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestCategory {
    CAREER("진로"),
    RELATION("관계"),
    SELF_REFLECTION("자기성찰"),
    EMOTION("감정"),
    GROWTH("성장"),
    ELSE("기타"),
    ALL("전체");

    private final String description;
}
