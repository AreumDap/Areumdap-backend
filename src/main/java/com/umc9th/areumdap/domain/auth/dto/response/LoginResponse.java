package com.umc9th.areumdap.domain.auth.dto.response;

public record LoginResponse(
        Long userId,
        String email,
        String name,
        String accessToken,
        String refreshToken
) {
}
