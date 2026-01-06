package com.umc9th.areumdap.common.status;

import com.umc9th.areumdap.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {

    // 예시
    COMMON_SUCCESS_STATUS(HttpStatus.OK, "COOMON_2OO", "성공적으로 처리되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
