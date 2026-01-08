package com.umc9th.areumdap.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.umc9th.areumdap.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static ResponseEntity<ApiResponse<Void>> success(
            BaseStatus successStatus
    ) {
        return ResponseEntity
                .status(successStatus.getHttpStatus())
                .body(new ApiResponse<>(true, successStatus.getCode(), successStatus.getMessage(), null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(
            BaseStatus successStatus,
            T data
    ) {
        return ResponseEntity
                .status(successStatus.getHttpStatus())
                .body(new ApiResponse<>(true, successStatus.getCode(),successStatus.getMessage(), data));
    }

    public static ResponseEntity<ApiResponse<Void>> error(
            BaseStatus errorStatus
    ){
        return ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(new ApiResponse<>(false, errorStatus.getCode(), errorStatus.getMessage(),null));
    }

    public static ResponseEntity<ApiResponse<Void>> error(
            BaseStatus errorStatus,
            String message
    ){
        return ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(new ApiResponse<>(false, errorStatus.getCode(), message, null));
    }

}
