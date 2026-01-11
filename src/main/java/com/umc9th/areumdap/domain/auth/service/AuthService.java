package com.umc9th.areumdap.domain.auth.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.jwt.JwtService;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.auth.dto.request.ConfirmEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.LoginRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SignUpRequest;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.auth.dto.response.ReissueAccessTokenResponse;
import com.umc9th.areumdap.domain.auth.token.RefreshTokenHasher;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final EmailVerificationService emailVerificationService;

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenHasher refreshTokenHasher;


    // 이메일 인증 코드 요청
    public void sendEmailVerificationCode(SendEmailVerificationCodeRequest request) {
        emailVerificationService.sendEmailVerificationCode(request.email());
    }

    // 이메일 인증 코드 확인
    public void confirmEmailVerificationCode(ConfirmEmailVerificationCodeRequest request) {
        emailVerificationService.confirmEmailVerificationCode(request.email(), request.verificationCode());
    }

    // 회원가입
    public void signUp(SignUpRequest request) {
        userQueryService.checkEmailNotExists(request.email());
        emailVerificationService.validateEmailVerified(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        userCommandService.registerUser(request.name(), request.birth(), request.email(), encodedPassword);
    }

    // 회원탈퇴
    public void withdraw(Long userId) {
        User user = userQueryService.getUserByIdAndDeletedFalse(userId);
        userCommandService.withdraw(user);
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = userQueryService.getUserByEmailAndDeletedFalse(request.email());
        validatePasswordMatch(request.password(), user.getPassword());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        userCommandService.updateRefreshToken(user, refreshTokenHasher.hash(refreshToken));
        return LoginResponse.from(user, accessToken, refreshToken);
    }

    // 로그아웃
    public void logout(Long userId) {
        User user = userQueryService.getUserByIdAndDeletedFalse(userId);
        userCommandService.clearRefreshToken(user);
    }

    // 토큰 재발급
    public ReissueAccessTokenResponse reissueAccessToken(String refreshToken) {
        Claims claims = jwtService.validateRefreshToken(refreshToken);
        User user = userQueryService.getUserByIdAndDeletedFalse(Long.parseLong(claims.getSubject()));

        if(!refreshTokenHasher.matches(refreshToken, user.getRefreshToken()))
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_MISMATCH);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        user.updateRefreshToken(refreshTokenHasher.hash(newRefreshToken));
        return new ReissueAccessTokenResponse(newAccessToken, newRefreshToken);
    }

    // 비밀번호 검증
    private void validatePasswordMatch(String inputPassword, String storedPassword) {
        if (!passwordEncoder.matches(inputPassword, storedPassword))
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
    }

}
