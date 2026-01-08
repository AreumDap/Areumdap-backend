package com.umc9th.areumdap.domain.auth.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.auth.dto.request.ConfirmEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.LoginRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SendEmailVerificationCodeRequest;
import com.umc9th.areumdap.domain.auth.dto.request.SignUpRequest;
import com.umc9th.areumdap.domain.auth.dto.response.LoginResponse;
import com.umc9th.areumdap.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/email-verification")
    @Operation(summary = "이메일 인증 코드 요청")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 인증 코드 요청 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 형식이 아닌 경우", content = @Content())
    })
    public ResponseEntity<ApiResponse<Void>> sendEmailVerificationCode(
            @Valid @RequestBody SendEmailVerificationCodeRequest emailVerificationRequest
    ) {
        authService.sendEmailVerificationCode(emailVerificationRequest);
        return ApiResponse.success(SuccessStatus.SEND_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @PostMapping("/email-verification/confirm")
    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 인증 코드 확인 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 형식이 아닌 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 코드가 만료되었거나 인증 코드가 일치하지 않은 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 이메일로 인증요청을 보내지 않은 경우", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> confirmEmailVerificationCode(
            @Valid @RequestBody ConfirmEmailVerificationCodeRequest confirmEmailVerificationCodeRequest
    ) {
        authService.confirmEmailVerificationCode(confirmEmailVerificationCodeRequest);
        return ApiResponse.success(SuccessStatus.CONFIRM_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 형식이나 비밀번호 형식이 맞지 않는 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "이메일 인증을 완료하지 않은 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 이메일을 가진 유저가 존재하는 경우", content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        authService.signUp(signUpRequest);
        return ApiResponse.success(SuccessStatus.SIGNUP_SUCCESS);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않는 경우", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content)
    })
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse response = authService.login(loginRequest);
        return ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, response);
    }

}
