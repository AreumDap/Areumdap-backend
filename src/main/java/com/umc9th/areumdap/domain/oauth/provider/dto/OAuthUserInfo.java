package com.umc9th.areumdap.domain.oauth.provider.dto;

import com.umc9th.areumdap.domain.user.enums.OAuthProvider;

public record OAuthUserInfo(
        OAuthProvider oauthProvider,
        String oauthId,
        String email,
        String nickname
) {
}
