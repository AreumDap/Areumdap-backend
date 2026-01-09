package com.umc9th.areumdap.domain.auth.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.auth.entity.EmailVerification;
import com.umc9th.areumdap.domain.auth.repository.EmailVerificationRepository;
import com.umc9th.areumdap.domain.auth.util.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailSender emailSender;
    private static final SecureRandom random = new SecureRandom();
    private final EmailVerificationRepository emailVerificationRepository;

    // 이메일 인증 코드 전송
    public void sendEmailVerificationCode(String email) {
        String verificationCode = createEmailVerificationCode();
        emailSender.sendEmailVerificationCode(email, verificationCode);

        emailVerificationRepository.findByEmail(email)
                .ifPresentOrElse(
                        exist ->
                                updateEmailVerificationCode(exist, verificationCode),
                        () ->
                    registerEmailVerification(email, verificationCode)
                );
    }

    // 이메일 인증 코드 확인
    public void confirmEmailVerificationCode(
            String email, String verificationCode
    ) {
        EmailVerification emailVerification = getEmailVerificationByEmail(email);

        validateCodeExpiration(emailVerification.getUpdatedAt());
        validateVerificationCode(emailVerification.getVerificationCode(), verificationCode);

        emailVerification.updateIsVerified(true);
    }

    // 회원가입 시 해당 이메일로 요청을 했는지 확인
    public void validateEmailVerified(String email) {
        if (!emailVerificationRepository.existsByEmailAndVerifiedTrue(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_NOT_VERIFIED);
        }
    }

    // EmailVerification 등록
    private void registerEmailVerification(
            String email, String verificationCode
    ) {
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .email(email)
                        .verificationCode(verificationCode)
                        .verified(false)
                        .build()
        );
    }

    // 이메일 정보 조회 (없을 경우 예외 발생)
    private EmailVerification getEmailVerificationByEmail(String email) {
        return emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EMAIL_NOT_FOUND));
    }

    // 인증 코드 만료 검증(5분)
    private void validateCodeExpiration(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();
        if(updatedAt.isBefore(now.minusMinutes(5)))
            throw new GeneralException(ErrorStatus.VERIFICATION_CODE_EXPIRED);
    }

    // 인증 코드 일치 여부 검증
    private void validateVerificationCode(String storedCode, String inputCode) {
        if(!storedCode.equals(inputCode))
            throw new GeneralException(ErrorStatus.INVALID_VERIFICATION_CODE);
    }

    // EmailVerification 수정(인증 코드, 인증 여부)
    private void updateEmailVerificationCode(
            EmailVerification emailVerification, String verificationCode
    ) {
        emailVerification.updateIsVerified(false);
        emailVerification.updateVerificationCode(verificationCode);
    }

    // 인증 코드 생성(6자리)
    private String createEmailVerificationCode() {
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }

}
