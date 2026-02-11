package com.umc9th.areumdap.domain.oauth.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record OAuthKakaoProperties(
        @NotBlank
        String clientId,

        @NotBlank
        String clientSecret,

        @NotBlank
        String redirectUri,

        @NotBlank
        String authBaseUrl,

        @NotBlank
        String apiBaseUrl
) {
    @Override
    public String toString() {
        return "OAuthKakaoProperties[" +
                "clientId=" + clientId +
                ", clientSecret=***" +
                ", redirectUri=" + redirectUri +
                ", authBaseUrl=" + authBaseUrl +
                ", apiBaseUrl=" + apiBaseUrl +
                ']';
    }
}
