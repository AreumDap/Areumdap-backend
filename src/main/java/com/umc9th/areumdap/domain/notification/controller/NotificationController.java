package com.umc9th.areumdap.domain.notification.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.notification.service.NotificationService;
import com.umc9th.areumdap.domain.user.dto.request.UpdateDeviceRequest;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.DeviceCommandService;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: 환경변수 적용
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService pushService;
    private final DeviceCommandService deviceCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> updateToken(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UpdateDeviceRequest updateDeviceRequest) {
        User user = this.userQueryService.getUserByIdAndDeletedFalse(userId);
        deviceCommandService.updateDevice(user, updateDeviceRequest);
        return ApiResponse.success(SuccessStatus.UPDATE_TOKEN_SUCCESS);
    }
}
