package com.umc9th.areumdap.domain.character.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.character.enums.QuestCategory;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Table(name = "quests")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false) // todo: enum 변경
    private QuestCategory category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    public int getRemainingDays() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), this.deadline);
    }
}
