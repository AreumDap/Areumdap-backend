package com.umc9th.areumdap.domain.auth.dto.response;

import com.umc9th.areumdap.domain.user.entity.User;

public record LoginResponse(
        Long userId,
        String email,
        String name,
        String accessToken,
        String refreshToken
) {
    public static LoginResponse from(
            User user,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                accessToken,
                refreshToken
        );
    }

}
