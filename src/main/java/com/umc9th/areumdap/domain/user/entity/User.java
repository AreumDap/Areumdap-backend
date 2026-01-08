package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "`user`")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id", length = 30)
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", columnDefinition = "oauth_provider_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OAuthProvider oauthProvider;

    @Column(name="name",length = 30)
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "age")
    private Long age;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", columnDefinition = "sex_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Sex sex;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    // 로그아웃 시 RefreshToken 제거
    public void clearRefreshToken(User user) {
        user.clearRefreshToken();
    }

    public void clearRefreshToken() {
        this.refreshToken = null;
    }

    // 탈퇴 처리
    public void withdraw() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.email = null;
        this.password = null;
        this.refreshToken = null;
        this.name = null;
        this.birth = null;
        this.age = null;
    }

}
