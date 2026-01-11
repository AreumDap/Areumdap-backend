package com.umc9th.areumdap.domain.oauth.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthNaverUserInfoResponse(
        String resultcode,
        String message,
        Response response
){
    public record Response(
            String id,
            String nickname,
            String name,
            String email,
            String gender,
            String age,
            String birthday,
            @JsonProperty("profile_image")
            String profileImage,
            String birthyear,
            String mobile
    ) {}
}
