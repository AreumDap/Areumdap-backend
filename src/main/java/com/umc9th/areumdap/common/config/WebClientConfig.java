package com.umc9th.areumdap.common.config;

import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import com.umc9th.areumdap.domain.oauth.properties.OAuthNaverProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private static final int MAX_IN_MEMORY_SIZE = 2 * 1024 * 1024;
    private final OAuthNaverProperties oAuthNaverProperties;
    private final OAuthKakaoProperties oAuthKakaoProperties;

    private WebClient buildWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE
                )
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(config ->
                                        config.defaultCodecs()
                                                .maxInMemorySize(MAX_IN_MEMORY_SIZE)
                                )
                                .build()
                )
                .build();
    }

    @Bean(name = "kakaoAuthWebClient")
    public WebClient oAuthKakaoWebClient() {
        return buildWebClient("https://kauth.kakao.com");
    }

    @Bean(name = "kakaoApiWebClient")
    public WebClient kakaoApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    @Bean(name = "naverAuthWebClient")
    public WebClient oAuthNaverWebClient() {
        return buildWebClient(oAuthNaverProperties.authBaseUrl());
    }

    @Bean(name = "naverApiWebClient")
    public WebClient naverApiWebClient() {
        return buildWebClient(oAuthNaverProperties.apiBaseUrl());
    }
}
