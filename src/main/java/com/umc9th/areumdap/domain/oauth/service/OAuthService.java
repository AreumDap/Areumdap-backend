package com.umc9th.areumdap.domain.oauth.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.jwt.JwtService;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.oauth.client.OAuthKakaoClient;
import com.umc9th.areumdap.domain.oauth.client.OAuthNaverClient;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthNaverLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthNaverLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.properties.OAuthNaverProperties;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthKakaoTokenResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthNaverTokenResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthUserInfo;
import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final JwtService jwtService;
    private final StringRedisTemplate stringRedisTemplate;

    private final OAuthKakaoClient oAuthKakaoClient;
    private final OAuthKakaoProperties oAuthKakaoProperties;
    private final OAuthNaverClient oAuthNaverClient;
    private final OAuthNaverProperties oAuthNaverProperties;


    private static final Duration STATE_TTL = Duration.ofMinutes(5);
    private static final String NAVER_STATE_KEY = "oauth:naver:state:";

    // 카카오 로그인 URL 생성
    public OAuthKakaoLoginUrlResponse getKakaoLoginUrl() {
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

    // 카카오 로그인 처리 후 JWT 토큰 생성
    public LoginResponse kakaoLogin(OAuthKakaoLoginRequest request) {
        OAuthKakaoTokenResponse kakaoToken = oAuthKakaoClient.getToken(request.code());
        OAuthUserInfo kakaoUserInfo = oAuthKakaoClient.getUserInfo(kakaoToken.accessToken());

        Optional<User> userOptional = userQueryService.getUserByOauthInfo(kakaoUserInfo);
        User user = userOptional.orElseGet(() -> userCommandService.registerKakaoUser(
                kakaoUserInfo.oauthId(),
                kakaoUserInfo.oauthProvider(),
                kakaoUserInfo.nickname(),
                kakaoUserInfo.email()
        ));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return  LoginResponse.from(user, accessToken,refreshToken);
    }

    // 네이버 로그인 URL 생성
    public OAuthNaverLoginUrlResponse getNaverLoginUrl() {
        String naverState = generateNaverState();
        saveNaverState(naverState);

        return new OAuthNaverLoginUrlResponse(
                UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                        .queryParam("client_id", oAuthNaverProperties.clientId())
                        .queryParam("redirect_uri", oAuthNaverProperties.redirectUri())
                        .queryParam("response_type", "code")
                        .queryParam("state", naverState)
                        .build()
                        .toUriString()
        );
    }

    // 네이버 로그인 처리 후 JWT 토큰 생성
    public LoginResponse naverLogin(OAuthNaverLoginRequest request) {
        validateAndDeleteNaverState(request.state());

        OAuthNaverTokenResponse naverToken = oAuthNaverClient.getToken(request.code(), request.state());
        OAuthUserInfo naverUserInfo = oAuthNaverClient.getUserInfo(naverToken.accessToken());

        Optional<User> userOptional = userQueryService.getUserByOauthInfo(naverUserInfo);
        User user = userOptional.orElseGet(() -> userCommandService.registerKakaoUser(
                naverUserInfo.oauthId(),
                naverUserInfo.oauthProvider(),
                naverUserInfo.nickname(),
                naverUserInfo.email()
        ));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.from(user, accessToken,refreshToken);
    }

    // Naver 로그인 창을 불러오기 위한 state값 생성
    private String generateNaverState(){
        byte[] bytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    // 네이버 로그인 콜백 후 state값 검증을 위한 Redis에 저장
    private void saveNaverState(String state){
        stringRedisTemplate.opsForValue()
                .set(NAVER_STATE_KEY + state, "1", STATE_TTL);
    }

    // Redis에 저장된 state값과 네이버 로그인 콜백으로 받은 state값을 비교 후 삭제
    private void validateAndDeleteNaverState(String state){
        String key = NAVER_STATE_KEY + state;

        Boolean exists = stringRedisTemplate.hasKey(key);
        if( exists == null || !exists)
            throw new GeneralException(ErrorStatus.BAD_REQUEST);

        stringRedisTemplate.delete(key);
    }

}
