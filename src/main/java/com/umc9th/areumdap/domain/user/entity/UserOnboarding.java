package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.user.enums.Season;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Table(name = "user_onboarding")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserOnboarding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    @Column(name = "seasons", columnDefinition = "season_enum[]", nullable = false)
    private List<Season> seasons;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "keywords", columnDefinition = "varchar(100)[]")
    private List<String> keywords;

    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "nickname", length = 10)
    private String nickname;

}
