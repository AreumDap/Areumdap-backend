package com.umc9th.areumdap.domain.device.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.device.dto.reqeust.RegisterDeviceRequest;
import com.umc9th.areumdap.domain.device.service.DeviceCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: 환경변수 적용
@RequestMapping("/api/device")
@RestController
@RequiredArgsConstructor
public class DeviceController implements DeviceControllerDocs{

    private final DeviceCommandService deviceCommandService;

    @Override
    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> registerDevice(
            @AuthenticationPrincipal Long userId,
            @Valid  @RequestBody RegisterDeviceRequest registerDeviceRequest
    ) {
        deviceCommandService.registerDevice(userId, registerDeviceRequest);
        return ApiResponse.success(SuccessStatus.REGISTER_DEVICE_SUCCESS);
    }

}
