package com.umc9th.areumdap.domain.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.naver")
public record OAuthNaverProperties(
        String clientId,
        String clientSecret,
        String redirectUri
) {
}
