package com.umc9th.areumdap.common.status;

import com.umc9th.areumdap.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseStatus {

    // 예시
    COMM_ERROR_STATUS(HttpStatus.BAD_REQUEST, "COMM_400", "잘못된 요청입니다."),

    /**
     * Common
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMM_400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMM_401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMM_403", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMM_404", "요청한 자원을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMM_405", "허용되지 않은 메소드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMM_500", "서버 내부 오류입니다."),

    /**
     * Auth
     */
    VERIFICATION_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증 코드가 만료되었습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증 코드가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "AUTH_401", "이메일 인증이 완료되지 않았습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_401", "비밀번호가 올바르지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404", "인증 요청한 이메일이 아닙니다."),
    EMAIL_NOT_FOUND2(HttpStatus.NOT_FOUND, "AUTH_404", "존재하지 않는 이메일입니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"AUTH_409", "이미 존재하는 이메일입니다."),
    SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500", "이메일 인증 코드 발송 중 오류가 발생했습니다.");

    /**
     * User
     */


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
