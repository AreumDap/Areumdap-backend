package com.umc9th.areumdap.domain.oauth.service;

import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthKakaoProperties oAuthKakaoProperties;

    // 카카오 로그인 URL 생성
    public OAuthKakaoLoginUrlResponse getOAuthKakaoLoginUrl() {
        return new OAuthKakaoLoginUrlResponse(
                UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", oAuthKakaoProperties.clientId())
                .queryParam("redirect_uri", oAuthKakaoProperties.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "profile_nickname, account_email")
                .build()
                .toUriString()
        );
    }

}
