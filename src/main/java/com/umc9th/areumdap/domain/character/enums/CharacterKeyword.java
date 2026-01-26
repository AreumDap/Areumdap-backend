package com.umc9th.areumdap.domain.character.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum CharacterKeyword {

    // 봄
    SOFT("부드러운"),
    KIND("다정한"),
    INNOCENT("낙천적인"),
    BRIGHT("밝은"),
    PURE("순수한"),
    SHY("수줍은"),
    LOVELY("상냥한"),
    YOUNG("어린"),
    CLEAR("투명한"),

    // 여름
    HOT("뜨거운"),
    VIVID("선명한"),
    HONEST("솔직한"),
    BOLD("대담한"),
    STRONG("강렬한"),
    ACTIVE("적극적인"),
    DRIVEN("추진력 있는"),
    CLEAR_CUT("분명한"),
    ENERGETIC("활발한"),

    // 가을/겨울 공통
    CALM("담담한"),
    QUIET("고요한"),
    RELAXED("느긋한"),
    DEEP("깊이 있는"),
    MATURE("성숙한"),
    DELICATE("섬세한"),
    STABLE("안정된"),
    RATIONAL("이성적인"),
    COMPOSED("침착한"),

    // 겨울 전용
    CONTROLLED("절제된"),
    COOL("냉정한"),
    FIRM("흔들림 없는"),
    WEIGHTED("무게 있는"),
    REALISTIC("현실적인");

    private final String korean;

    CharacterKeyword(String korean) {
        this.korean = korean;
    }

    public static Optional<CharacterKeyword> fromKorean(String korean) {
        return Arrays.stream(values())
                .filter(k -> k.korean.equals(korean))
                .findFirst();
    }

}

