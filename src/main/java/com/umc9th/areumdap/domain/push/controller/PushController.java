package com.umc9th.areumdap.domain.push.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.push.dto.PushRequest;
import com.umc9th.areumdap.domain.push.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: 환경변수 적용
@RestController
@RequestMapping("push")
@RequiredArgsConstructor
public class PushController {

    private final com.umc9th.areumdap.domain.push.service.FcmService fcmService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> setToken() {
        return ApiResponse.success(SuccessStatus.UPDATE_TOKEN_SUCCESS);
    }

    @PostMapping("/send")
    public String sendPush(@RequestBody PushRequest request) {
        return fcmService.sendMessage(
                request.getToken(),
                request.getTitle(),
                request.getBody(),
                request.getData()
        );
    }

    @PostMapping("/token")
    public void saveToken(@RequestBody Map<String, String> body) {
        System.out.println("받은 토큰 = " + body.get("token"));
    }
}
