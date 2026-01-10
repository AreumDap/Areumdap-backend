package com.umc9th.areumdap.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final int MAX_IN_MEMORY_SIZE = 2 * 1024 * 1024;

    private WebClient baseWebClient(String baseUrl) {
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
        return baseWebClient("https://kauth.kakao.com");
    }

    @Bean(name = "kakaoApiWebClient")
    public WebClient kakaoApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    @Bean(name = "naverAuthWebClient")
    public WebClient oAuthNaverWebClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean(name = "naverApiWebClient")
    public WebClient naverApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .build();
    }
}
