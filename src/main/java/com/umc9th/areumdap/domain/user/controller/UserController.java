package com.umc9th.areumdap.domain.user.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.user.controller.docs.UserControllerDocs;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.service.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserCommandService userCommandService;

    @Override
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<Void>> registerUserOnboarding(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterUserOnboardingRequest registerUserOnboardingRequest
    ) {
        userCommandService.registerUserOnboarding(userId, registerUserOnboardingRequest);
        return ApiResponse.success(SuccessStatus.REGISTER_USER_ONBOARDING_SUCCESS);
    }

}
