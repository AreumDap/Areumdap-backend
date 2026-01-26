package com.umc9th.areumdap.domain.character.enums;

import lombok.Getter;

import java.util.Set;

import static com.umc9th.areumdap.domain.character.enums.CharacterKeyword.*;

@Getter
public enum CharacterSeason {

    SPRING(Set.of(
            SOFT, KIND, INNOCENT, BRIGHT, PURE,
            SHY, LOVELY, YOUNG, CLEAR
    )),

    SUMMER(Set.of(
            HOT, VIVID, HONEST, BOLD, STRONG,
            ACTIVE, DRIVEN, CLEAR_CUT, ENERGETIC
    )),

    FALL(Set.of(
            CALM, QUIET, RELAXED, DEEP, MATURE,
            DELICATE, STABLE, RATIONAL, COMPOSED
    )),

    WINTER(Set.of(
            QUIET, CONTROLLED, SOFT, COOL,
            FIRM, WEIGHTED, MATURE, CALM,
            REALISTIC
    ));

    private final Set<CharacterKeyword> allowedKeywords;

    CharacterSeason(Set<CharacterKeyword> allowedKeywords) {
        this.allowedKeywords = allowedKeywords;
    }

    public boolean supports(CharacterKeyword keyword) {
        return allowedKeywords.contains(keyword);
    }

}


