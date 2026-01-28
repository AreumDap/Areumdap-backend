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
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증 코드가 일치하지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증 코드가 만료되었습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "AUTH_401", "이메일 인증이 완료되지 않았습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_401", "비밀번호가 올바르지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404", "인증 요청한 이메일이 아닙니다."),
    EMAIL_NOT_FOUND2(HttpStatus.NOT_FOUND, "AUTH_404", "존재하지 않는 이메일입니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"AUTH_409", "이미 존재하는 이메일입니다."),
    SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500", "이메일 인증 코드 발송 중 오류가 발생했습니다."),

    /**
     * User
     */
    INVALID_USER_NOTIFICATION_SETTING(HttpStatus.BAD_REQUEST,"USER_400", "알림이 켜져 있을 경우 알림 시간은 필수입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저가 존재하지 않습니다."),
    USER_ONBOARDING_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저 온보딩 정보를 찾을 수 없습니다."),
    USER_ONBOARDING_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409", "유저 온보딩이 이미 존재합니다."),


    /**
     * Notification
     * */
    NOTIFICATION_SENDING_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "NOTI_500", "FCM 푸시 알림을 보내는 과정에서 오류가 발생하였습니다."),

    /**
     * JWT
     */
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_401", "토큰이 존재하지 않습니다."),
    JWT_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT_401", "잘못된 JWT 서명입니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT_401", "잘못된 JWT 형식입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_401", "만료된 JWT 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "JWT_401", "지원되지 않는 JWT 토큰입니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT_401", "JWT 토큰이 잘못되었습니다."),
    JWT_EXTRACT_ID_FAILED(HttpStatus.UNAUTHORIZED, "JWT_401", "토큰에서 사용자 정보를 추출할 수 없습니다."),
    JWT_GENERAL_ERROR(HttpStatus.UNAUTHORIZED, "JWT_401", "JWT 토큰 처리 중 알 수 없는 오류가 발생했습니다."),
    JWT_INVALID_TYPE(HttpStatus.UNAUTHORIZED, "JWT_401", "토큰 타입이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_401", "DB에 저장된 토큰과 일치하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "JWT_401", "리프레시 토큰 정보가 사용자 정보와 일치하지 않습니다."),
    JWT_EXTRACT_ROLE_FAILED(HttpStatus.UNAUTHORIZED, "JWT_401", "토큰에서 사용자 Role을 추출할 수 없습니다."),

    /**
     * AWS
     * */
    SQS_MESSAGE_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AWS_500", "SQS 처리 중 오류가 발생하였습니다."),
    
    /**
     * Character
     */
    CHARACTER_LEVEL_UP_REQUIRED(HttpStatus.BAD_REQUEST, "CHAR_400", "단계 업그레이드가 필요합니다."),
    CHARACTER_GROWTH_NOT_ENOUGH_XP(HttpStatus.BAD_REQUEST, "CHAR_400", "아직 성장할 준비가 되지 않았어요. XP를 더 모아주세요!"),
    CHARACTER_LEVEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAR_400", "존재하지 않는 단계입니다."),
    CHARACTER_SEASON_MUST_BE_NOT_NULL(HttpStatus.BAD_REQUEST, "CHAR_400", "계절은 필수 입력 값입니다."),
    CHARACTER_KEYWORDS_MUST_BE_NOT_NULL(HttpStatus.BAD_REQUEST, "CHAR_400", "키워드는 필수 입력 값입니다."),
    CHARACTER_KEYWORDS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "CHAR_400", "키워드는 최대 3개까지 선택 가능합니다."),
    INVALID_CHARACTER_KEYWORD(HttpStatus.BAD_REQUEST,"CHAR_400","잘못된 키워드 값입니다"),
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAR_404", "캐릭터 정보를 찾을 수 없습니다."),
    CHARACTER_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAR_404", "캐릭터 이미지를 찾을 수 없습니다."),
    CHARACTER_ALREADY_MAX_LEVEL(HttpStatus.CONFLICT, "CHAR_409", "이미 최고 단계로 성장했습니다."),
    CHARACTER_ALREADY_EXISTS(HttpStatus.CONFLICT, "CHAR_409", "캐릭터가 이미 존재합니다."),

    /**
     * Cursor
     */
    CURSOR_BAD_REQUEST(HttpStatus.BAD_REQUEST,"MISS_400","cursorTime과 cursorId는 함께 전달되어야 합니다."),

    /**
     * Mission
     */
    MISSION_FORBIDDEN(HttpStatus.FORBIDDEN, "MISSION_403", "해당 성찰과제에 접근할 권한이 없습니다."),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION_404", "존재하지 않는 과제입니다."),
    MISSION_ALREADY_COMPLETED(HttpStatus.CONFLICT, "MISSION_409", "이미 완료한 과제입니다."),

    /**
     * Chat
     */
    CHAT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND,"HISTORY_404","chatHistory를 찾을 수 없습니다."),
    CHAT_THREAD_NOT_FOUND(HttpStatus.NOT_FOUND,"THREAD_404","채팅창을 찾을 수 없습니다."),
    CHAT_THREAD_ACCESS_DENIED(HttpStatus.FORBIDDEN,"THREAD_403", "채팅 스레드 접근 권한이 없습니다"),

    /**
     * Question
     */
    ALREADY_SAVED_QUESTION(HttpStatus.BAD_REQUEST,"QUESTION_400","이미 저장한 질문입니다."),
    QUESTION_BANK_NOT_FOUND(HttpStatus.NOT_FOUND,"QUESTION_404","존재하지 않는 질문은행 입니다."),
    USER_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_QUESTION_404","존재하지 않는 유저 질문입니다."),

    /**
     *  Report
     */
    CHAT_REPORT_NOT_FOUND(HttpStatus.NOT_FOUND,"REPORT_404","존재하지 않는 레포트입니다."),

    /**
     * ChatBot
     */
    CHATBOT_RESPONSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"CHATBOT_500", "챗봇 응답 생성에 실패했습니다"),
    QUESTION_BANK_NOT_ENOUGH(HttpStatus.UNPROCESSABLE_ENTITY, "CHATBOT_422", "배정 가능한 질문이 부족합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
