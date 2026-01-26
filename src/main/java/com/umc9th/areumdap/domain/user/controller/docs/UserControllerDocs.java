package com.umc9th.areumdap.domain.user.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import com.umc9th.areumdap.domain.user.dto.request.RegisterUserOnboardingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserNotificationSettingRequest;
import com.umc9th.areumdap.domain.user.dto.request.UpdateUserProfileRequest;
import com.umc9th.areumdap.domain.user.dto.response.GetUserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PatchMapping("/profile")
    @Operation(summary = "유저 프로필 수정")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 프로필 수정 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 값이 잘못된 경우", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저가 존재하지 않는 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> updateUserProfile(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest
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

    @Operation(
            summary = "채팅방 목록 조회",
            description = """
                 유저의 채팅방 목록을 조회합니다.
                
                - 기본 정렬: 최신순 (updatedAt DESC)
                - favorite=true: 즐겨찾기 채팅방만 최신순으로 조회
                - 무한 스크롤을 위한 커서 기반 페이징을 사용합니다.
                
                 커서 규칙
                - 첫 페이지: cursorTime, cursorId 없이 요청
                - 다음 페이지: 이전 응답의 nextCursorTime, nextCursorId 전달
                """
    )
    @GetMapping("/threads")
    ResponseEntity<ApiResponse<UserChatThreadCursorResponse>> getUserChatThreads(
            @AuthenticationPrincipal Long userId,
            @Parameter(
                    description = "true면 즐겨찾기 채팅방만 조회, false면 전체 최신순 조회"
            )
            @RequestParam(defaultValue = "false") boolean favorite,
            @Parameter(
                    description = """
                        커서 기반 페이징 요청 정보
                        
                        - cursorTime: 이전 페이지 마지막 채팅의 updatedAt
                        - cursorId: 이전 페이지 마지막 채팅의 ID
                        - size: 한 번에 가져올 채팅방 개수
                        
                         cursorTime과 cursorId는 반드시 함께 전달되어야 합니다.
                        """
            )
            @Valid @ModelAttribute UserChatThreadCursorRequest cursor
    );
}
