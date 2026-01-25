package com.umc9th.areumdap.domain.character.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum CharacterKeyword {

    // SPRING
    SOFT(Season.SPRING, "부드러운"),
    KIND(Season.SPRING, "다정한"),
    INNOCENT(Season.SPRING, "난천적인"),
    BRIGHT(Season.SPRING, "밝은"),
    PURE(Season.SPRING, "순수한"),
    CALM(Season.SPRING, "수줍은"),
    LOVELY(Season.SPRING, "상냥한"),
    YOUNG(Season.SPRING, "어린"),
    CLEAR(Season.SPRING, "투명한"),

    // SUMMER
    HOT(Season.SUMMER, "뜨거운"),
    VIVID(Season.SUMMER, "선명한"),
    FAST(Season.SUMMER, "솔직한"),
    BOLD(Season.SUMMER, "대담한"),
    STRONG(Season.SUMMER, "강렬한"),
    ACTIVE(Season.SUMMER, "적극적인"),
    DRIVEN(Season.SUMMER, "추진력 있는"),
    CLEAR_CUT(Season.SUMMER, "분명한"),
    ENERGETIC(Season.SUMMER, "활발한"),

    // FALL
    WARM(Season.FALL, "담담한"),
    QUIET(Season.FALL, "고요한"),
    RELAXED(Season.FALL, "느긋한"),
    DEEP(Season.FALL, "깊이 있는"),
    MATURE(Season.FALL, "성숙한"),
    DELICATE(Season.FALL, "섬세한"),
    STABLE(Season.FALL, "안정된"),
    RATIONAL(Season.FALL, "이성적인"),
    CALM_FALL(Season.FALL, "침착한"),

    // WINTER
    ELEGANT(Season.WINTER, "고요한"),
    CONTROLLED(Season.WINTER, "절제된"),
    GENTLE(Season.WINTER, "부드러운"),
    COOL(Season.WINTER, "냉정한"),
    FIRM(Season.WINTER, "흔들림 없는"),
    WEIGHTED(Season.WINTER, "무게 있는"),
    REFINED(Season.WINTER, "성숙한"),
    RESERVED(Season.WINTER, "담담한"),
    REALISTIC(Season.WINTER, "현실적인");

    private final Season season;
    private final String korean;

    CharacterKeyword(Season season, String korean) {
        this.season = season;
        this.korean = korean;
    }

    public static String normalize(String input) {
        if (input == null) return null;

        // 영어 key로 들어온 경우
        Optional<CharacterKeyword> byName = Arrays.stream(values())
                .filter(k -> k.name().equalsIgnoreCase(input))
                .findFirst();

        if (byName.isPresent()) {
            return byName.get().name();  // 영어 key로 통일
        }

        // 한글로 들어온 경우
        Optional<CharacterKeyword> byKorean = Arrays.stream(values())
                .filter(k -> k.korean.equals(input))
                .findFirst();

        if (byKorean.isPresent()) {
            return byKorean.get().name(); // 영어 key로 통일
        }

        // enum에 없는 자유 입력
        return input;
    }



}
