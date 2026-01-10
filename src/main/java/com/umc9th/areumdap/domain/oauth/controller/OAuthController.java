package com.umc9th.areumdap.domain.oauth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.oauth.dto.response.OAuthKakaoLoginUrlResponse;
import com.umc9th.areumdap.domain.oauth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "소셜 인증 API")
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
        OAuthKakaoLoginUrlResponse response = oauthService.getOAuthKakaoLoginUrl();
        return ApiResponse.success(SuccessStatus.GET_KAKAO_LOGIN_URL_SUCCESS,response);
    }

}

