package com.umc9th.areumdap.domain.oauth.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthKakaoUserInfoResponse(
    Long id,

    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            String email,
            Profile profile
    ){}

    public record Profile(
            String nickname
    ){}
}
