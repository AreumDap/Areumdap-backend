package com.umc9th.areumdap.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserOnboardingRequest(
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이하여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임에 특수문자는 사용할 수 없습니다.")
        String nickname
) {
}
