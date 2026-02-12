package com.umc9th.areumdap.domain.auth.dto.response;

public record ReissueAccessTokenResponse(
        String accessToken,
        String refreshToken
) {
}
