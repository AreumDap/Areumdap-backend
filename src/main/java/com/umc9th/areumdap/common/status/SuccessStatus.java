package com.umc9th.areumdap.common.status;

import com.umc9th.areumdap.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {

    // 예시
    COMM_SUCCESS_STATUS(HttpStatus.OK, "COMM_200", "성공적으로 처리되었습니다."),

    /**
     * Auth
     */
    SEND_EMAIL_VERIFICATION_CODE_SUCCESS(HttpStatus.OK, "AUTH_200", "이메일 인증 코드 발송 성공"),
    CONFIRM_EMAIL_VERIFICATION_CODE_SUCCESS(HttpStatus.OK, "AUTH_200", "이메일 인증 성공"),
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH_200", "로그인 성공"),
    WITHDRAW_SUCCESS(HttpStatus.OK, "AUTH_200", "회원탈퇴 성공"),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH_201", "회원가입 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
