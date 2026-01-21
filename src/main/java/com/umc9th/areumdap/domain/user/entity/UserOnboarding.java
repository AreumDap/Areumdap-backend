package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.user.enums.Season;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    private Season season;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "keywords", columnDefinition = "text[]")
    private List<String> keywords;

    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "nickname", length = 10)
    private String nickname;

    public UserOnboarding(User user, Season season) {
        this.user = user;
        this.season = season;
    }

    public void updateOnboarding(Season season, List<String> keywords, Long characterId) {
        this.season = season;
        this.keywords = keywords;
        this.characterId = characterId;
    }

    public void completeOnboarding(Season season, List<String> keywords, Long characterId, String nickname) {
        this.season = season;
        this.keywords = keywords;
        this.characterId = characterId;
        this.nickname = nickname;
    }

}
