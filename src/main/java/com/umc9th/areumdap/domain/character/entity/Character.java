package com.umc9th.areumdap.domain.character.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
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
    @JoinColumn(name = "user_id", nullable = false)
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
}
