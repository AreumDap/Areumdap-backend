package com.umc9th.areumdap.domain.oauth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.oauth.controller.docs.OAuthControllerDocs;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthNaverLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthNaverLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.service.OAuthKakaoService;
import com.umc9th.areumdap.domain.oauth.service.OAuthNaverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController implements OAuthControllerDocs {

    private final OAuthNaverService oauthNaverService;
    private final OAuthKakaoService oAuthKakaoService;

    @Override
    @GetMapping("/kakao/login-uri")
    public ResponseEntity<ApiResponse<OAuthKakaoLoginUrlResponse>> getOAuthKakaoLoginUrl() {
        OAuthKakaoLoginUrlResponse response = oAuthKakaoService.getKakaoLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_KAKAO_LOGIN_URL_SUCCESS, response);
    }

    @Override
    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<LoginResponse>> oAuthKakaoLogin(
            @Valid @RequestBody OAuthKakaoLoginRequest oauthKakaoLoginRequest
    ) {
        LoginResponse response = oAuthKakaoService.kakaoLogin(oauthKakaoLoginRequest);
        return ApiResponse.success(SuccessStatus.KAKAO_LOGIN_SUCCESS, response);
    }

    @Override
    @GetMapping("/naver/login-uri")
    public ResponseEntity<ApiResponse<OAuthNaverLoginUrlResponse>> getOAuthNaverLoginUrl() {
        OAuthNaverLoginUrlResponse response = oauthNaverService.getNaverLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_NAVER_LOGIN_URL_SUCCESS, response);
    }

    @Override
    @PostMapping("/naver/login")
    public ResponseEntity<ApiResponse<LoginResponse>> oAuthNaverLogin(
            @Valid @RequestBody OAuthNaverLoginRequest oauthNaverLoginRequest
    ) {
        LoginResponse response = oauthNaverService.naverLogin(oauthNaverLoginRequest);
        return ApiResponse.success(SuccessStatus.NAVER_LOGIN_SUCCESS, response);
    }

}

