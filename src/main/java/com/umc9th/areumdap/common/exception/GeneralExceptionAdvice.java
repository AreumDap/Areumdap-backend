package com.umc9th.areumdap.common.exception;

import com.umc9th.areumdap.common.base.BaseStatus;
import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GeneralExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(
            GeneralException e
    ) {
        if(e.getErrorStatus().getHttpStatus().is5xxServerError()){
            log.error("[*] GeneralException :", e);
        }
        else{
            log.error("[*] GeneralException : {}", e.getMessage());
        }
        return ApiResponse.error(e.getErrorStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        String errorMessage = "잘못된 요청입니다: " + e.getMessage();
        log.error("[*] IllegalArgumentException :", e);
        return ApiResponse.error(ErrorStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(
            NullPointerException e
    ) {
        String errorMessage = "서버에서 예기치 않은 오류가 발생했습니다. 요청을 처리하는 중에 Null 값이 참조되었습니다.";
        log.error("[*] NullPointerException :", e);
        return ApiResponse.error(ErrorStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e
    ) {
        log.error("[*] Internal Server Error :", e);
        return ApiResponse.error(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected  ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest
    ){
        BaseStatus errorStatus = ErrorStatus.BAD_REQUEST;
        String errorMessage = e.getBindingResult().getFieldErrors().isEmpty()
                ?errorStatus.getMessage()
                : e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ApiResponse<Void> body = createApiResponse(errorStatus, errorMessage);
        return handleExceptionInternal(e, body, headers, statusCode, webRequest);
    }

    private ApiResponse<Void> createApiResponse(
            BaseStatus errorStatus, String errorMessage
    ) {
        return new ApiResponse<>(
                false,
                errorStatus.getCode(),
                (errorMessage != null? errorMessage : errorStatus.getMessage()),
                null
        );
    }

}
