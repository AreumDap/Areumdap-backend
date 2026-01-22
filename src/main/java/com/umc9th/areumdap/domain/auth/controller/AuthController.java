package com.umc9th.areumdap.domain.auth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.controller.docs.AuthControllerDocs;
import com.umc9th.areumdap.domain.auth.dto.request.ConfirmEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.LoginRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SignUpRequest;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.auth.dto.response.ReissueAccessTokenResponse;
import com.umc9th.areumdap.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponse<Void>> sendEmailVerificationCode(
            @Valid @RequestBody SendEmailVerificationCodeRequest emailVerificationRequest
    ) {
        authService.sendEmailVerificationCode(emailVerificationRequest);
        return ApiResponse.success(SuccessStatus.SEND_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @Override
    @PostMapping("/email-verification/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEmailVerificationCode(
            @Valid @RequestBody ConfirmEmailVerificationCodeRequest confirmEmailVerificationCodeRequest
    ) {
        authService.confirmEmailVerificationCode(confirmEmailVerificationCodeRequest);
        return ApiResponse.success(SuccessStatus.CONFIRM_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        authService.signUp(signUpRequest);
        return ApiResponse.success(SuccessStatus.SIGNUP_SUCCESS);
    }

    @Override
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal Long userId
    ) {
        authService.withdraw(userId);
        return ApiResponse.success(SuccessStatus.WITHDRAW_SUCCESS);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse response = authService.login(loginRequest);
        return ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, response);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal Long userId
    ) {
        authService.logout(userId);
        return ApiResponse.success(SuccessStatus.LOGOUT_SUCCESS);
    }

    @Override
    @PostMapping("/token/reissue")
    public ResponseEntity<ApiResponse<ReissueAccessTokenResponse>> reissueAccessToken(
            @RequestHeader("Authorization") String token
    ) {
        String refreshToken = token.substring(7).trim();

        ReissueAccessTokenResponse response = authService.reissueAccessToken(refreshToken);
        return ApiResponse.success(SuccessStatus.REISSUE_TOKEN_SUCCESS, response);
    }

}
