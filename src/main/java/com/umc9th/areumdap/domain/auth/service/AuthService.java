package com.umc9th.areumdap.domain.auth.service;

import com.umc9th.areumdap.domain.auth.dto.request.ConfirmEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailVerificationService emailVerificationService;

    // 이메일 인증 코드 요청
    public void sendEmailVerificationCode(SendEmailVerificationCodeRequest request) {
        emailVerificationService.sendEmailVerificationCode(request.email());
    };

    // 이메일 인증 코드 확인
    public void confirmEmailVerificationCode(ConfirmEmailVerificationCodeRequest request) {
        emailVerificationService.confirmEmailVerificationCode(request.email(), request.verificationCode());
    }

}
