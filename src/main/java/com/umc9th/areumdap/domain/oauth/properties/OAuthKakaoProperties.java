package com.umc9th.areumdap.domain.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record OAuthKakaoProperties(
        String clientId,
        String clientSecret,
        String redirectUri,
        String authBaseUrl,
        String apiBaseUrl
) {
}
