package com.umc9th.areumdap.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateDeviceRequest(
            @NotBlank(message = "디바이스 토큰은 필수 입니다.")
            String deviceToken,

            @NotBlank(message = "OS 타입은 필수입니다.")
            String osType
) {}
