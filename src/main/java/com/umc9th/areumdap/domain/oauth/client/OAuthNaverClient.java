package com.umc9th.areumdap.domain.oauth.client;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.oauth.properties.OAuthNaverProperties;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthNaverTokenResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthNaverUserInfoResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthUserInfo;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthNaverClient {
    @Qualifier("naverAuthWebClient")
    private final WebClient naverAuthWebClient;

    @Qualifier("naverApiWebClient")
    private final WebClient naverApiWebClient;

    private final OAuthNaverProperties naverProperties;

    /* ===== 1. code → access token ===== */
    public OAuthNaverTokenResponse getToken(String code, String state) {

        String url = buildTokenRequestUrl(code, state);
        OAuthNaverTokenResponse response = naverAuthWebClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OAuthNaverTokenResponse.class)
                .onErrorResume(e -> {
                    log.error("[NAVER][TOKEN] token api call failed. message={}", e.getMessage(), e);
                    return Mono.error(new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR));
                })
                .block();

        validateTokenResponse(response);
        return response;
    }

    /* ===== 2. 액세스 토큰 → 사용자 정보 ===== */
    public OAuthUserInfo getUserInfo(String accessToken) {

        OAuthNaverUserInfoResponse response =
                naverApiWebClient.get()
                        .uri("/v1/nid/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::isError,
                                r -> r.bodyToMono(String.class)
                                        .flatMap(body -> handleUserInfoError(r.statusCode(), body))
                        )
                        .bodyToMono(OAuthNaverUserInfoResponse.class)
                        .block();

        if (response == null || response.response() == null) {
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }

        OAuthNaverUserInfoResponse.Response user = response.response();
        log.info("[NAVER][USER] user={}", user);

        return new OAuthUserInfo(
                OAuthProvider.NAVER,
                user.id(),
                user.email(),
                user.nickname() != null
                        ? user.nickname()
                        : user.name()
        );
    }

    // Access Token 발급 URL 생성
    private String buildTokenRequestUrl(String code, String state) {
        return UriComponentsBuilder
                .fromUriString(naverProperties.authBaseUrl())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverProperties.clientId())
                .queryParam("client_secret", naverProperties.clientSecret())
                .queryParam("code", code)
                .queryParam("state", state)
                .build(true)
                .toUriString();
    }

    // 응답 값 검증
    private void validateTokenResponse(OAuthNaverTokenResponse response) {
        if (response == null ||
                response.error() != null ||
                !StringUtils.hasText(response.accessToken())) {

            log.error("[NAVER][TOKEN] invalid response: {}", response);
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 에러처리
    private Mono<? extends Throwable> handleUserInfoError(
            HttpStatusCode status,
            String body
    ) {
        log.error("[NAVER][USER][{}] {}", status.value(), body);

        if (status.is4xxClientError()) {
            return Mono.error(new GeneralException(ErrorStatus.UNAUTHORIZED));
        }
        return Mono.error(new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR));
    }

}
