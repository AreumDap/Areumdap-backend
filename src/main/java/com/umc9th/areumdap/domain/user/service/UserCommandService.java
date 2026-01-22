package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNotificationSettingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserProfileRequest;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserOnboardingCommandService userOnboardingCommandService;
    private final CharacterRepository characterRepository;

    // 유저 등록
    public void registerUser(String name, LocalDate birth, String email, String password) {
        Long age = (long) (LocalDate.now().getYear() - birth.getYear() + 1);
        userRepository.save(
                User.builder()
                        .oauthProvider(OAuthProvider.EMAIL)
                        .name(name)
                        .birth(birth)
                        .age(age)
                        .email(email)
                        .password(password)
                        .device(null)
                        .deleted(false)
                        .build()
        );
    }

    // 소셜 유저 등록 or 찾기
    public User getOrRegisterUser(String oauthId, OAuthProvider provider, String name, String email) {
        User user = userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
                .orElseGet(() -> {
                    if (userRepository.existsByEmailAndDeletedFalse(email)) {
                        throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
                    }
                    return userRepository.save(User.builder()
                            .oauthId(oauthId)
                            .oauthProvider(provider)
                            .name(name)
                            .email(email)
                            .deleted(false)
                            .build());
                });

        user.updateProfile(name, email);
        return user;
    }

    // 유저 온보딩 저장
    public Long registerUserOnboarding(Long userId, RegisterUserOnboardingRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Character character = characterRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        return userOnboardingCommandService.registerUserOnboarding(
                user,
                request.season(),
                request.keywords(),
                character.getId(),
                request.nickname()
        );
    }

    // 유저 알림 값 수정
    public void updateUserNotificationSetting(Long userId, UpdateUserNotificationSettingRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));


        if (request.notificationEnabled() && request.notificationTime() == null)
            throw new GeneralException(ErrorStatus.INVALID_USER_NOTIFICATION_SETTING);
        user.updateNotificationSetting(request.notificationEnabled(), request.notificationTime());
    }

    // 유저 프로필 수정
    public void updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateBirth(request.birth());
    }

    // 회원탈퇴 시 소프트 delete 방식 적용
    public void withdraw(User user) {
        user.withdraw();
    }

    // 로그아웃 시 RefreshToken 제거
    public void clearRefreshToken(User user) {
        user.clearRefreshToken();
    }

    // 유저 리프레시 토큰 업데이트
    public void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
    }

}
