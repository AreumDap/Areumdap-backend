package com.umc9th.areumdap.domain.oauth.client;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthKakaoTokenResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthKakaoUserInfoResponse;
import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthUserInfo;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthKakaoClient {

    @Qualifier("kakaoAuthWebClient")
    private final WebClient kakaoAuthWebClient;

    @Qualifier("kakaoApiWebClient")
    private final WebClient kakaoApiWebClient;

    private final OAuthKakaoProperties kakaoProperties;

    /* ===== 1. 인가 코드 → 액세스 토큰 ===== */
    public OAuthKakaoTokenResponse getToken(String code) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", kakaoProperties.clientId());
        form.add("redirect_uri", kakaoProperties.redirectUri());
        form.add("code", code);

        if (StringUtils.hasText(kakaoProperties.clientSecret())) {
            form.add("client_secret", kakaoProperties.clientSecret());
        }

        try {
            OAuthKakaoTokenResponse response =
                    kakaoAuthWebClient.post()
                            .uri("/oauth/token")
                            .body(BodyInserters.fromFormData(form))
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::is4xxClientError,
                                    r -> r.bodyToMono(String.class)
                                            .map(b -> {
                                                log.error("Kakao token 4xx: {}", b);
                                                return new GeneralException(ErrorStatus.BAD_REQUEST);
                                            })
                            )
                            .onStatus(
                                    HttpStatusCode::is5xxServerError,
                                    r -> r.bodyToMono(String.class)
                                            .map(b -> {
                                                log.error("Kakao token 5xx: {}", b);
                                                return new GeneralException(
                                                        ErrorStatus.INTERNAL_SERVER_ERROR);
                                            })
                            )
                            .bodyToMono(OAuthKakaoTokenResponse.class)
                            .block();

            if (response == null || !StringUtils.hasText(response.accessToken())) {
                throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
            }

            return response;

        } catch (WebClientResponseException e) {
            log.error("Kakao token error: {}", e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* ===== 2. 액세스 토큰 → 사용자 정보 ===== */
    public OAuthUserInfo getUserInfo(String accessToken) {

        try {
            OAuthKakaoUserInfoResponse response =
                    kakaoApiWebClient.get()
                            .uri("/v2/user/me")
                            .header("Authorization", "Bearer " + accessToken)
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::is4xxClientError,
                                    r -> r.bodyToMono(String.class)
                                            .map(b -> {
                                                log.error("Kakao user 4xx: {}", b);
                                                return new GeneralException(ErrorStatus.UNAUTHORIZED);
                                            })
                            )
                            .onStatus(
                                    HttpStatusCode::is5xxServerError,
                                    r -> r.bodyToMono(String.class)
                                            .map(b -> {
                                                log.error("Kakao user 5xx: {}", b);
                                                return new GeneralException(
                                                        ErrorStatus.INTERNAL_SERVER_ERROR);
                                            })
                            )
                            .bodyToMono(OAuthKakaoUserInfoResponse.class)
                            .block();

            if (response == null || response.id() == null) {
                throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
            }

            String email = null;
            String nickname = null;

            if (response.kakaoAccount() != null) {
                email = response.kakaoAccount().email();
                if (response.kakaoAccount().profile() != null) {
                    nickname = response.kakaoAccount().profile().nickname();
                }
            }

            return new OAuthUserInfo(
                    OAuthProvider.KAKAO,
                    String.valueOf(response.id()),
                    email,
                    nickname
            );

        } catch (WebClientResponseException e) {
            log.error("Kakao user error: {}", e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

