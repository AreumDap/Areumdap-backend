package com.umc9th.areumdap.domain.oauth.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthNaverLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthNaverLoginUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OAuth", description = "소셜 인증 API")
public interface OAuthControllerDocs {

    @GetMapping("/kakao/login-uri")
    @Operation(summary = "카카오 로그인 URL 조회", description = "카카오 로그인 창을 띄우기 위한 URL 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카카오 로그인 URL 조회 성공", content = @Content(schema = @Schema(implementation = OAuthKakaoLoginUrlResponse.class)))
    })
    ResponseEntity<ApiResponse<OAuthKakaoLoginUrlResponse>> getOAuthKakaoLoginUrl();

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 처리 후 JWT 토큰 발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 이메일로 회원가입을 한 경우", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    ResponseEntity<ApiResponse<LoginResponse>> oAuthKakaoLogin(
            @Valid @RequestBody OAuthKakaoLoginRequest oauthKakaoLoginRequest
    );

    @GetMapping("/kakao/test/login")
    void kakaoTestCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletResponse response
    );

    @GetMapping("/naver/login-uri")
    @Operation(summary = "네이버 로그인 URL 조회", description = "네이버 로그인 창을 띄우기 위한 URL 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "네이버 로그인 URL 조회 성공", content = @Content(schema = @Schema(implementation = OAuthNaverLoginUrlResponse.class)))
    })
    ResponseEntity<ApiResponse<OAuthNaverLoginUrlResponse>> getOAuthNaverLoginUrl();

    @PostMapping("/naver/login")
    @Operation(summary = "네이버 로그인", description = "네이버 로그인 처리 후 JWT 토큰 발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "네이버 로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 이메일로 회원가입을 한 경우", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    ResponseEntity<ApiResponse<LoginResponse>> oAuthNaverLogin(
            @Valid @RequestBody OAuthNaverLoginRequest oauthNaverLoginRequest
    );

}
