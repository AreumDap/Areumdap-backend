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

import java.time.LocalDate;

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
        User existingUser = userQueryService.getUserByEmail(request.email());

        if (existingUser != null) {
            if (existingUser.isDeleted()) {
                reactivateEmailUser(existingUser, request);
                return;
            }
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        emailVerificationService.validateEmailVerified(request.email());
        registerNewUser(request);
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

        if (!refreshTokenHasher.matches(refreshToken, user.getRefreshToken()))
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_MISMATCH);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        userCommandService.updateRefreshToken(user, refreshTokenHasher.hash(newRefreshToken));
        return new ReissueAccessTokenResponse(newAccessToken, newRefreshToken);
    }

    // 테스트 계정 생성 및 로그인
    public LoginResponse testLogin() {

        // 1. 현재 test 계정 개수 조회
        long count = userQueryService.countTestUsers();
        int nextNumber = (int) (count + 1);

        String name = "유저" + nextNumber;
        String email = "test" + nextNumber + "@gmail.com";
        String password = "test" + nextNumber;
        LocalDate birth = LocalDate.of(2002, 5, 29);

        // 2. 혹시 이미 존재하면 재사용
        User user = userQueryService.getUserByEmailAndOauthProvider(email);
        if (user == null) {
            user = registerNewUser(SignUpRequest.from(name, birth, email, password));
        }

        // 3. 토큰 발급
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        userCommandService.updateRefreshToken(user, refreshTokenHasher.hash(refreshToken));
        return LoginResponse.from(user, accessToken, refreshToken);
    }

    // 비밀번호 검증
    private void validatePasswordMatch(String inputPassword, String storedPassword) {
        if (!passwordEncoder.matches(inputPassword, storedPassword))
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
    }

    // 이메일 유저 복구
    private void reactivateEmailUser(User user, SignUpRequest request) {
        user.restore();
        user.updateEmailProfile(request.name(), request.birth(), request.email());
        user.updatePassword(passwordEncoder.encode(request.password()));
    }

    // 새로운 유저 등록
    private User registerNewUser(SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        return userCommandService.registerUser(
                request.name(),
                request.birth(),
                request.email(),
                encodedPassword
        );
    }

}
