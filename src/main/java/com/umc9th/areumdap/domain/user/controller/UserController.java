package com.umc9th.areumdap.domain.user.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 API")
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/onboarding")
    @Operation(summary = "유저 온보딩 저장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 온보딩 저장 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    public ResponseEntity<ApiResponse<Void>> registerUserOnboarding(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterUserOnboardingRequest registerUserOnboardingRequest
    ) {
        userCommandService.registerUserOnboarding(userId, registerUserOnboardingRequest);
        return ApiResponse.success(SuccessStatus.REGISTER_USER_ONBOARDING_SUCCESS);
    }
}
