package com.umc9th.areumdap.domain.push.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.push.dto.PushRequest;
import com.umc9th.areumdap.domain.push.service.PushService;
import com.umc9th.areumdap.domain.user.dto.command.DeviceTokenCommand;
import com.umc9th.areumdap.domain.user.service.DeviceTokenCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: 환경변수 적용
@RestController
@RequestMapping("push")
@RequiredArgsConstructor
public class PushController {

    private final PushService pushService;
    private final DeviceTokenCommandService  deviceTokenCommandService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> updateToken(
            @AuthenticationPrincipal Long userId,
            @Valid DeviceTokenCommand deviceTokenCommand) {
        deviceTokenCommandService.updateDeviceToken(userId, deviceTokenCommand);
        return ApiResponse.success(SuccessStatus.UPDATE_TOKEN_SUCCESS);
    }
}
