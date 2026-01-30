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
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH_200", "로그아웃 성공"),
    REISSUE_TOKEN_SUCCESS(HttpStatus.OK, "AUTH_200", "토큰 재발급 성공"),
    WITHDRAW_SUCCESS(HttpStatus.OK, "AUTH_200", "회원탈퇴 성공"),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH_201", "회원가입 성공"),

    /**
     * OAuth
     */
    GET_KAKAO_LOGIN_URL_SUCCESS(HttpStatus.OK, "OAUTH_200", "카카오 로그인 URL 조회 성공"),
    GET_NAVER_LOGIN_URL_SUCCESS(HttpStatus.OK, "OAUTH_200", "네이버 로그인 URL 조회 성공"),
    KAKAO_LOGIN_SUCCESS(HttpStatus.OK, "OAUTH_200", "카카오 로그인 성공"),
    NAVER_LOGIN_SUCCESS(HttpStatus.OK, "OAUTH_200", "네이버 로그인 성공"),

    /**
     * User
     */
    REGISTER_USER_ONBOARDING_SUCCESS(HttpStatus.OK, "USER_200", "유저 온보딩 등록 성공"),
    GET_USER_PROFILE_SUCCESS(HttpStatus.OK,"USER_200","유저 프로필 조회 성공"),
    UPDATE_USER_NOTIFICATION_SETTING_SUCCESS(HttpStatus.OK,"USER_200","유저 알림 세팅값 수정 성공"),
    UPDATE_USER_BIRTH_SUCCESS(HttpStatus.OK,"USER_200","유저 생년월일 수정 성공"),
    UPDATE_USER_NICKNAME_SUCCESS(HttpStatus.OK,"USER_200","유저 닉네임 수정 성공"),

    /**
     * Device
     */
    REGISTER_DEVICE_SUCCESS(HttpStatus.OK, "DEVICE_200", "기기 정보 등록 성공"),

    /**
     * Character
     */
    GET_CHARACTER_MAIN_SUCCESS(HttpStatus.OK, "CHAR_200", "캐릭터 메인 조회 성공"),
    GET_CHARACTER_HISTORY_SUCCESS(HttpStatus.OK, "CHAR_200", "성장 히스토리 조회 성공"),
    REGISTER_CHARACTER_SUCCESS(HttpStatus.CREATED, "CHAR_201", "캐릭터 생성에 성공했습니다."),
    CHARACTER_GROWTH_SUCCESS(HttpStatus.CREATED, "CHAR_201", "캐릭터 성장 성공"),

    /**
     *  Question
     */
    CREATE_QUESTION_SUCCESS(HttpStatus.CREATED,"QUES_201","질문 저장 성공"),
    GET_ALL_SAVED_QUESTION_SUCCESS(HttpStatus.OK,"QUES_200","저장된 질문 조회 성공"),

    /**
     * Mission
     */
    GET_MISSION_DETAIL_SUCCESS(HttpStatus.OK, "MISSION_200", "성찰과제 상세 조회 성공"),
    COMPLETE_MISSION_SUCCESS(HttpStatus.CREATED, "MISSION_201", "과제 수행 완료! XP가 지급되었습니다."),


    /**
     * ChatBot
     */
    GET_CHATBOT_RECOMMEND_SUCCESS(HttpStatus.OK, "CHATBOT_200", "AI 대화 질문 추천 조회 성공"),

    /**
     *  Mission
     */
    CREATE_MISSION_SUCCESS(HttpStatus.CREATED, "MISSION_201", "성찰과제 생성 성공"),
    GET_ALL_COMPLETED_MISSION_SUCCESS(HttpStatus.OK,"MISS_200","완료한 과제 조회 완료"),

    /**
     *  Chat
     */
    CREATE_CHAT_THREAD_SUCCESS(HttpStatus.CREATED, "CHAT_201", "채팅 스레드 생성 성공"),
    SEND_CHAT_MESSAGE_SUCCESS(HttpStatus.OK, "CHAT_200","메시지 전송 성공"),
    GET_USER_CHAT_THREADS_SUCCESS(HttpStatus.OK,"CHAT_200","유저 채팅창 목록 조회 성공"),
    GET_CHAT_HISTORIES_SUCCESS(HttpStatus.OK,"CHAT_200","채팅 기록 조회 성공"),
    GET_CHAT_REPORT_SUCCESS(HttpStatus.OK,"CHAT_200","채팅 레포트 조회 성공"),
    UPDATE_FAVORITE_SUCCESS(HttpStatus.OK,"CHAT_200","즐겨찾기 상태 변경 성공"),
    DELETE_CHAT_THREAD_SUCCESS(HttpStatus.OK,"CHAT_200","대화 스레드 삭제 성공"),
    GENERATE_CHAT_SUMMARY_SUCCESS(HttpStatus.OK,"CHAT_200","대화 요약 생성 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    }
