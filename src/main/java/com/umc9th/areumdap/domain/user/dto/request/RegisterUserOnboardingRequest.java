package com.umc9th.areumdap.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserOnboardingRequest(
        @NotBlank
        @Size(max = 10)
        String nickname
) {
}
