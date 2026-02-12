package com.umc9th.areumdap.domain.character.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.enums.CharacterSeason;
import com.umc9th.areumdap.domain.user.entity.User;
import jakarta.persistence.*;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Table(name = "character")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "level", nullable = false)
    @Builder.Default
    private Integer level = 1;

    @Column(name = "current_xp", nullable = false)
    @Builder.Default
    private Integer currentXp = 0;

    @Column(name = "goal_xp", nullable = false)
    @Builder.Default
    private Integer goalXp = CharacterLevel.LEVEL_1.getGoalXp();

    @Column(name = "past_description", columnDefinition = "TEXT")
    @Builder.Default
    private String pastDescription = "";

    @Column(name = "present_description", columnDefinition = "TEXT")
    @Builder.Default
    private String presentDescription = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "season", columnDefinition = "season_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CharacterSeason characterSeason;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "keywords", columnDefinition = "text[]")
    private List<String> keywords;

    @OneToMany(mappedBy = "character",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<CharacterHistory> histories = new ArrayList<>();


    // 캐릭터 객체 생성
    public Character(User user) {
        this.user = user;
        this.level = 1;
        this.currentXp = 0;
        this.goalXp = CharacterLevel.LEVEL_1.getGoalXp();
    }

    // 레벨업
    public void tryLevelUp() {
        if (this.isMaxLevel()) {
            throw new GeneralException(ErrorStatus.CHARACTER_ALREADY_MAX_LEVEL);
        }

        if (this.currentXp < this.goalXp) {
            throw new GeneralException(ErrorStatus.CHARACTER_GROWTH_NOT_ENOUGH_XP);
        }

        int nextGoalXp = CharacterLevel.getGoalXpByLevel(this.level + 1);
        this.currentXp -= this.goalXp;
        this.level++;
        this.goalXp = nextGoalXp;
    }

    // 경험치 증가
    public void addXp(int amount) {
        this.currentXp += amount;
    }

    // 최고 레벨인지 검사
    public boolean isMaxLevel() {
        return this.level >= CharacterLevel.LEVEL_4.getLevel();
    }

    // 성장 요약 업데이트
    public void updateHistorySummary(String pastDescription, String presentDescription) {
        this.pastDescription = pastDescription;
        this.presentDescription = presentDescription;
    }

    // 캐릭터 생성
    public static Character create(User user, CharacterSeason characterSeason, List<String> keywords) {
        return Character.builder()
                .user(user)
                .characterSeason(characterSeason)
                .keywords(keywords)
                .build();
    }

}
