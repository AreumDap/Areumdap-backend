package com.umc9th.areumdap.domain.oauth.provider.dto;

import com.umc9th.areumdap.domain.user.enums.OAuthProvider;

public record OAuthUserInfo(
        OAuthProvider oauthProvider,
        String oauthId,
        String email,
        String nickname
) {
    @Override
    public String toString() {
        return "OAuthUserInfo[" +
                "oauthProvider=" + oauthProvider +
                ", oauthId=***" +
                ", email=***" +
                ", nickname=" + nickname +
                ']';
    }
}
