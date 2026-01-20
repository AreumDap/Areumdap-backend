package com.umc9th.areumdap.domain.character.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum CharacterLevel {
    LEVEL_1(1, 70),
    LEVEL_2(2, 100),
    LEVEL_3(3, 150),
    LEVEL_4(4, 200);

    private final int level;
    private final int goalXp;

    public static Optional<CharacterLevel> findByLevel(int level) {
        return Arrays.stream(values())
                .filter(characterLevel -> characterLevel.level == level)
                .findFirst();
    }

    public static int getGoalXpByLevel(int level) {
        return findByLevel(level)
                .map(CharacterLevel::getGoalXp)
                .orElse(LEVEL_4.getGoalXp());
    }
}
