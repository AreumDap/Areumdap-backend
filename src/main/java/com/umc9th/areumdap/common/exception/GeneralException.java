package com.umc9th.areumdap.common.exception;

import com.umc9th.areumdap.common.base.BaseStatus;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final BaseStatus errorStatus;

    public GeneralException(
            BaseStatus errorStatus
    ) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
