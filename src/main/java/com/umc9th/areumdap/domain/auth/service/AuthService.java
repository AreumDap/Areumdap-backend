package com.umc9th.areumdap.domain.auth.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.auth.dto.request.ConfirmEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.LoginRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SignUpRequest;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final EmailVerificationService emailVerificationService;

    private final PasswordEncoder passwordEncoder;

    // 이메일 인증 코드 요청
    public void sendEmailVerificationCode(SendEmailVerificationCodeRequest request) {
        emailVerificationService.sendEmailVerificationCode(request.email());
    };

    // 이메일 인증 코드 확인
    public void confirmEmailVerificationCode(ConfirmEmailVerificationCodeRequest request) {
        emailVerificationService.confirmEmailVerificationCode(request.email(), request.verificationCode());
    }

    // 회원가입
    public void signUp(SignUpRequest request) {
        userQueryService.checkEmailNotExists(request.email());
        emailVerificationService.validateEmailVerified(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());

        userCommandService.registerUser(request.name(),request.birth(), request.email(),encodedPassword);
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = userQueryService.getUserByEmail(request.email());
        validatePasswordMatch(user.getEmail(), passwordEncoder.encode(request.password()));
        return null;
    }

    private void validatePasswordMatch(String storedPassword, String inputPassword){
        if(!passwordEncoder.matches(inputPassword, storedPassword))
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
    }

}
