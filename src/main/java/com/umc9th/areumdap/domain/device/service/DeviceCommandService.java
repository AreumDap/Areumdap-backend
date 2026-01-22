package com.umc9th.areumdap.domain.device.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.device.dto.reqeust.RegisterDeviceRequest;
import com.umc9th.areumdap.domain.device.entity.Device;
import com.umc9th.areumdap.domain.device.repository.DeviceRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceCommandService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    // 디바이스 정보 등록
    public void registerDevice(Long userId, RegisterDeviceRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Device device = deviceRepository.findByUserId(userId)
                .orElseGet(() -> Device.builder()
                        .token(request.deviceToken())
                        .osType(request.osType())
                        .userId(userId).
                        build()
                );

        device.updateDevice(request.deviceToken(),request.osType());
        deviceRepository.save(device);

        user.updateDevice(device);
    }

}
