package com.umc9th.areumdap.domain.character.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "character_history")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CharacterHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "level", nullable = false)
    private Integer level;
}
