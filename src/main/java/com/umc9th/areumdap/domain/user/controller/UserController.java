package com.umc9th.areumdap.domain.user.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import com.umc9th.areumdap.domain.chat.service.UserChatThreadQueryService;
import com.umc9th.areumdap.domain.user.controller.docs.UserControllerDocs;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNotificationSettingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserProfileRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNicknameRequest;
import com.umc9th.areumdap.domain.user.dto.response.GetUserProfileResponse;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final UserChatThreadQueryService userChatThreadQueryService;

    @Override
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<Void>> registerUserOnboarding(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterUserOnboardingRequest registerUserOnboardingRequest
    ) {
        userCommandService.registerUserOnboarding(userId, registerUserOnboardingRequest);
        return ApiResponse.success(SuccessStatus.REGISTER_USER_ONBOARDING_SUCCESS);
    }

    @Override
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal Long userId
    ) {
        GetUserProfileResponse response = userQueryService.getUserProfile(userId);
        return ApiResponse.success(SuccessStatus.GET_USER_PROFILE_SUCCESS, response);
    }

    @Override
    @PatchMapping("/birth")
    public ResponseEntity<ApiResponse<Void>> updateUserBirth(
            @AuthenticationPrincipal  Long userId,
            @Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest
    ) {
        userCommandService.updateUserBirth(userId, updateUserProfileRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_USER_PROFILE_SUCCESS);
    }

    @Override
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> updateUserNickname(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserNicknameRequest updateUserNicknameRequest
    ) {
        userCommandService.updateUserNickname(userId, updateUserNicknameRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_USER_NICKNAME_SUCCESS);
    }

    @Override
    @PatchMapping("/notification")
    public ResponseEntity<ApiResponse<Void>> updateUserNotificationSetting(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserNotificationSettingRequest updateUserNotificationSettingRequest
    ) {
        userCommandService.updateUserNotificationSetting(userId, updateUserNotificationSettingRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_USER_NOTIFICATION_SETTING_SUCCESS);
    }

    @Override
    @GetMapping("/chat/threads")
    public ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getUserChatThreads(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "false") boolean favorite,
            @Valid @ModelAttribute UserChatThreadCursorRequest cursor
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_USER_CHAT_THREADS_SUCCESS,
                userChatThreadQueryService.getThreads(userId, favorite, cursor)
        );
    }

}
