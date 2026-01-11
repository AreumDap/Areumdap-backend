package com.umc9th.areumdap.domain.oauth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthNaverLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthNaverLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.service.OAuthKakaoService;
import com.umc9th.areumdap.domain.oauth.service.OAuthNaverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "OAuth", description = "소셜 인증 API")
@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthNaverService oauthNaverService;
    private final OAuthKakaoService oAuthKakaoService;

    @GetMapping("/kakao/login-uri")
    @Operation(summary = "카카오 로그인 URL 조회", description = "카카오 로그인 창을 띄우기 위한 URL 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카카오 로그인 URL 조회 성공", content = @Content(schema = @Schema(implementation = OAuthKakaoLoginUrlResponse.class)))
    })
    public ResponseEntity<ApiResponse<OAuthKakaoLoginUrlResponse>> getOAuthKakaoLoginUrl() {
        OAuthKakaoLoginUrlResponse response = oAuthKakaoService.getKakaoLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_KAKAO_LOGIN_URL_SUCCESS, response);
    }

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 처리 후 JWT 토큰 발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 이메일로 회원가입을 한 경우", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginResponse>> oAuthKakaoLogin(
            @Valid @RequestBody OAuthKakaoLoginRequest oauthKakaoLoginRequest
    ) {
        LoginResponse response = oAuthKakaoService.kakaoLogin(oauthKakaoLoginRequest);
        return ApiResponse.success(SuccessStatus.KAKAO_LOGIN_SUCCESS, response);
    }

    @GetMapping("/kakao/test/login")
    public void kakaoTestCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletResponse response
    ) throws IOException {

        // 그냥 화면에 보여주기
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(
                error != null
                        ? "OAuth ERROR = " + error
                        : "OAuth CODE = " + code
        );
    }

    @GetMapping("/naver/login-uri")
    @Operation(summary = "네이버 로그인 URL 조회", description = "네이버 로그인 창을 띄우기 위한 URL 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "네이버 로그인 URL 조회 성공", content = @Content(schema = @Schema(implementation = OAuthNaverLoginUrlResponse.class)))
    })
    public ResponseEntity<ApiResponse<OAuthNaverLoginUrlResponse>> getOAuthNaverLoginUrl() {
        OAuthNaverLoginUrlResponse response = oauthNaverService.getNaverLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_NAVER_LOGIN_URL_SUCCESS, response);
    }

    @GetMapping("/naver/login")
    @Operation(summary = "네이버 로그인 콜백", description = "네이버 로그인 콜백 후 JWT 발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "네이버 로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 이메일로 회원가입을 한 경우", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginResponse>> oAuthNaverLogin(
            @RequestParam String code,
            @RequestParam String state
    ) {
        LoginResponse response = oauthNaverService.naverLogin(new OAuthNaverLoginRequest(code, state));
        return ApiResponse.success(SuccessStatus.NAVER_LOGIN_SUCCESS, response);
    }

}

