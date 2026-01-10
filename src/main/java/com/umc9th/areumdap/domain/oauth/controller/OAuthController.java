package com.umc9th.areumdap.domain.oauth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.oauth.dto.request.OAuthKakaoLoginRequest;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.service.OAuthService;
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

    private final OAuthService oauthService;

    @GetMapping("/kakao/login-uri")
    @Operation(summary = "카카오 로그인 URL 조회", description = "카카오 로그인 URL 창을 띄우기 위한 URL 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "카카오 로그인 URL 조회 성공", content = @Content(schema = @Schema(implementation = OAuthKakaoLoginUrlResponse.class)))
    })
    public ResponseEntity<ApiResponse<OAuthKakaoLoginUrlResponse>> getOAuthKakaoLoginUrl(){
        OAuthKakaoLoginUrlResponse response = oauthService.getKakaoLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_KAKAO_LOGIN_URL_SUCCESS,response);
    }

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 처리 후 JWT 토큰 발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "카카오 로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginResponse>> oAuthKakaoLogin(
            @Valid @RequestBody OAuthKakaoLoginRequest oauthKakaoLoginRequest
            ) {
        LoginResponse response = oauthService.kakaoLogin(oauthKakaoLoginRequest);
        return ApiResponse.success(SuccessStatus.KAKAO_LOGIN_SUCCESS,response);
    }

    @GetMapping("/kakao/test/login")
    public void kakaoTestCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletResponse response
    ) throws IOException {

        log.info("OAuth test callback code = {}", code);
        log.info("OAuth test callback error = {}", error);

        // 그냥 화면에 보여주기
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(
                error != null
                        ? "OAuth ERROR = " + error
                        : "OAuth CODE = " + code
        );
    }

}

