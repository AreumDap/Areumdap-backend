package com.umc9th.areumdap.domain.oauth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OAuthNaverLoginRequest(
        @NotBlank
        String code,

        @NotBlank
        String state
) {
}
