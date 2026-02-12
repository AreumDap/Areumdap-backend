package com.umc9th.areumdap.domain.user.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserBirthRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNotificationSettingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNicknameRequest;
import com.umc9th.areumdap.domain.user.dto.response.GetUserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 API")
public interface UserControllerDocs {

    @PostMapping("/onboarding")
    @Operation(summary = "유저 온보딩 저장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 온보딩 저장 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 온보딩이 이미 존재하는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> registerUserOnboarding(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterUserOnboardingRequest registerUserOnboardingRequest
    );

    @GetMapping("/profile")
    @Operation(summary = "유저 프로필 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 프로필 조회 성공", content = @Content(schema = @Schema(implementation = GetUserProfileResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<GetUserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal Long userId
    );

    @PatchMapping("/birth")
    @Operation(summary = "유저 생년월일 수정")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 생년월일 수정 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 값이 잘못된 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> updateUserBirth(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserBirthRequest updateUserBirthRequest
    );

    @PatchMapping("/nickname")
    @Operation(summary = "유저 닉네임 수정")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 닉네임 수정 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 값이 잘못된 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> updateUserNickname(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserNicknameRequest updateUserNicknameRequest
    );

    @PatchMapping("/notification")
    @Operation(summary = "유저 알림 관련 값 수정")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 알림 관련 값 수정 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 값이 잘못된 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> updateUserNotificationSetting(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserNotificationSettingRequest updateUserNotificationSettingRequest
    );

}
