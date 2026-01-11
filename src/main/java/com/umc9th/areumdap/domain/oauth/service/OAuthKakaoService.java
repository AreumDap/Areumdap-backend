package com.umc9th.areumdap.domain.oauth.service;

import com.umc9th.areumdap.common.jwt.JwtService;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.auth.token.RefreshTokenHasher;
import com.umc9th.areumdap.domain.oauth.client.OAuthKakaoClient;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthKakaoTokenResponse;
import com.umc9th.areumdap.domain.oauth.provider.dto.OAuthUserInfo;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthKakaoService {

    private final OAuthKakaoClient oAuthKakaoClient;
    private final OAuthKakaoProperties oAuthKakaoProperties;
    private final RefreshTokenHasher refreshTokenHasher;

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final JwtService jwtService;

    // 카카오 로그인 URL 생성
    public OAuthKakaoLoginUrlResponse getKakaoLoginUrl() {
        return new OAuthKakaoLoginUrlResponse(
                UriComponentsBuilder.fromUriString(oAuthKakaoProperties.authBaseUrl() + "/oauth/authorize")
                        .queryParam("client_id", oAuthKakaoProperties.clientId())
                        .queryParam("redirect_uri", oAuthKakaoProperties.redirectUri())
                        .queryParam("response_type", "code")
                        .queryParam("scope", "profile_nickname,account_email")
                        .build()
                        .toUriString()
        );
    }

    // 카카오 로그인 처리 후 JWT 토큰 생성
    public LoginResponse kakaoLogin(OAuthKakaoLoginRequest request) {
        OAuthKakaoTokenResponse kakaoToken = oAuthKakaoClient.getToken(request.code());
        OAuthUserInfo kakaoUserInfo = oAuthKakaoClient.getUserInfo(kakaoToken.accessToken());

        Optional<User> userOptional = userQueryService.getUserByOauthInfo(kakaoUserInfo);
        User user = userOptional.orElseGet(() -> userCommandService.registerOAuthUser(
                kakaoUserInfo.oauthId(),
                kakaoUserInfo.oauthProvider(),
                kakaoUserInfo.nickname(),
                kakaoUserInfo.email()
        ));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        userCommandService.updateRefreshToken(user,refreshTokenHasher.hash(refreshToken));
        return LoginResponse.from(user, accessToken, refreshToken);
    }

}
