package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "user")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class user extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id", length = 30)
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "oauth_provider",
            nullable = false,
            columnDefinition = "oauth_provider_enum"
    )
    private OAuthProvider oauthProvider;

    @Column(length = 30)
    private String nickname;

    @Column()
    private Long age;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "sex",
            nullable = false,
            columnDefinition = "sex_enum"
    )
    private Sex sex;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public void updateRefreshToken(String newRefreshToken){
        this.refreshToken = newRefreshToken;
    }

}
