package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNotificationSettingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserBirthRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNicknameRequest;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;

    // 유저 등록
    public User registerUser(String name, LocalDate birth, String email, String password) {
        Long age = (long) (LocalDate.now().getYear() - birth.getYear() + 1);
        return userRepository.save(
                User.builder()
                        .oauthProvider(OAuthProvider.EMAIL)
                        .name(name)
                        .birth(birth)
                        .age(age)
                        .email(email)
                        .password(password)
                        .deleted(false)
                        .build()
        );
    }

    // 소셜 유저 등록 or 찾기
    public User getOrRegisterUser(String oauthId, OAuthProvider provider, String name, String email) {
        return userRepository
                .findByOauthIdAndOauthProvider(oauthId, provider)
                .map(user -> {
                    reactivateIfDeleted(user);
                    user.updateProfile(name, email);
                    return user;
                })
                .orElseGet(() -> registerSocialUser(oauthId, provider, name, email));
    }

    // 유저 온보딩 저장
    public void registerUserOnboarding(Long userId, RegisterUserOnboardingRequest request) {
        User user = getUser(userId);

        if (user.isOnboardingCompleted())
            throw new GeneralException(ErrorStatus.USER_ONBOARDING_ALREADY_EXISTS);

        if (characterRepository.findByUser(user).isEmpty())
            throw new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND);

        user.updateNickname(request.nickname());
        user.completeOnboarding();
    }

    // 유저 알림 값 수정
    public void updateUserNotificationSetting(Long userId, UpdateUserNotificationSettingRequest request) {
        User user = getUser(userId);

        if (request.notificationEnabled() && request.notificationTime() == null)
            throw new GeneralException(ErrorStatus.INVALID_USER_NOTIFICATION_SETTING);
        user.updateNotificationSetting(request.notificationEnabled(), request.notificationTime());
    }

    // 유저 생년월일 수정
    public void updateUserBirth(Long userId, UpdateUserBirthRequest request) {
        User user = getUser(userId);
        user.updateBirth(request.birth());
    }

    // 유저 닉네임 수정
    public void updateUserNickname(Long userId, UpdateUserNicknameRequest request) {
        User user = getUser(userId);
        user.updateNickname(request.nickname());
    }

    // 회원탈퇴 시 소프트 delete 방식 적용
    public void withdraw(User user) {
        log.info("탈퇴전 유저의 정보 : {}{}", user.isDeleted(), user.getDeletedAt());
        user.withdraw();
        log.info("탈퇴된 유저의 정보 : {}{}", user.isDeleted(), user.getDeletedAt());
    }

    // 로그아웃 시 RefreshToken 제거
    public void clearRefreshToken(User user) {
        user.clearRefreshToken();
    }

    // 유저 리프레시 토큰 업데이트
    public void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
    }

    // 유저 정보 가져오기
    public User getUser(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 소셜 로그인 유저가 탈퇴했다가 다시 로그인 하는 경우 탈퇴 정책 복구
    private void reactivateIfDeleted(User user) {
        if (user.isDeleted())
            user.restore();
    }

    // 소셜 로그인 유저 등록
    private User registerSocialUser(String oauthId, OAuthProvider provider, String name, String email) {
        validateEmailDuplication(email);

        User user = User.createSocialUser(oauthId, provider, name, email);
        return userRepository.save(user);
    }

    // 중복 이메일 검사
    private void validateEmailDuplication(String email) {
        if (userRepository.existsByEmailAndDeletedFalse(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
    }

}
