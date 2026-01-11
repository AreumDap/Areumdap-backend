package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
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
                        .deleted(false)
                        .build()
        );
    }

    // 카카오 유저 등록
    public User registerOAuthUser(String oauthId, OAuthProvider provider, String name, String email) {
        return userRepository.save(
                User.builder()
                        .oauthId(oauthId)
                        .oauthProvider(provider)
                        .name(name)
                        .email(email)
                        .deleted(false)
                        .build()
        );
    }

    // 유저 온보딩 저장
    public void registerUserOnboarding(Long userId, RegisterUserOnboardingRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        // TODO 나나가 캐릭터 관련 작업하고 캐릭터 아이디가 실제로 존재하는지 검증하는 로직 필요
        userOnboardingCommandService.registerUserOnboarding(user, request.seasons(), request.keywords(), request.characterId(), request.nickname());
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
