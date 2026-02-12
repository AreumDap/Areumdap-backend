package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.enums.Sex;
import com.umc9th.areumdap.domain.character.entity.Character;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "oauth_id")
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", columnDefinition = "oauth_provider_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OAuthProvider oauthProvider;

    @Column(name = "name", length = 30)
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

    @Column(name = "notification_enabled", nullable = false)
    private boolean notificationEnabled;

    @Column(name = "notification_time")
    private LocalTime notificationTime;

    @Column(name = "onboarding_completed", nullable = false)
    @Builder.Default
    private boolean onboardingCompleted = false;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserChatThread> userChatThreads = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestion> userQuestions = new ArrayList<>();

    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Character character;

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    // 로그아웃 시 RefreshToken 제거
    public void clearRefreshToken() {
        this.refreshToken = null;
    }

    // 탈퇴 처리
    public void withdraw() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.refreshToken = null;
    }

    // 프로필 업데이트
    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // 알림 값 업데이트
    public void updateNotificationSetting(boolean enabled, LocalTime time) {
        this.notificationEnabled = enabled;
        this.notificationTime = enabled ? time : null;
    }

    // 생일 업데이트
    public void updateBirth(LocalDate birth) {
        this.birth = birth;
        this.age = calculateAge(birth);
    }

    // 나이 계산
    private Long calculateAge(LocalDate birth) {
        int currentYear = LocalDate.now().getYear();
        return (long) (currentYear - birth.getYear() + 1); // 한국 나이
    }

    // 온보딩 완료여부 체크
    public void completeOnboarding() {
        this.onboardingCompleted = true;
    }

    // 닉네임 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 탈퇴 복구
    public void restore(){
        this.deleted = false;
        this.deletedAt = null;
        this.onboardingCompleted = false;
        this.notificationEnabled = false;
        this.notificationTime = null;
    }

    // 소셜 로그인 유저 생성
    public static User createSocialUser(String oauthId, OAuthProvider provider, String name, String email){
        return User.builder()
                .oauthId(oauthId)
                .oauthProvider(provider)
                .name(name)
                .email(email)
                .deleted(false)
                .build();
    }

    // 비밀번호 업데이트
    public void updatePassword(String password) {
        this.password = password;
    }

    // 프로필 업데이트
    public void updateEmailProfile(String name, LocalDate birth, String email) {
        this.name = name;
        this.birth = birth;
        this.email = email;
    }

}
