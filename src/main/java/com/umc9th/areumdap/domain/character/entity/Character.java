package com.umc9th.areumdap.domain.character.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.entity.User;
import jakarta.persistence.*;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import lombok.*;

@Table(name = "characters")
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
    private String pastDescription;

    @Column(name = "present_description", columnDefinition = "TEXT")
    private String presentDescription;

    public Character(User user) {
        this.user = user;
        this.level = 1;
        this.currentXp = 0;
        this.goalXp = CharacterLevel.LEVEL_1.getGoalXp();
    }

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

    public void addXp(int amount) {
        this.currentXp += amount;
    }

    public boolean isMaxLevel() {
        return this.level >= CharacterLevel.LEVEL_4.getLevel();
    }
}
