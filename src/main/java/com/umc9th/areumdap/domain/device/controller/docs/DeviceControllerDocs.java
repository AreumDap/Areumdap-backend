package com.umc9th.areumdap.domain.device.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.device.dto.request.RegisterDeviceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Device", description = "기기 관련 API")
public interface DeviceControllerDocs {

    @PostMapping("")
    @Operation(summary = "기기 토큰 값, 종류 등록")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "기기 정보 등록 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "값을 제대로 요청하지 않은 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> registerDevice(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterDeviceRequest registerDeviceRequest
    );

}
