package com.umc9th.areumdap.domain.device.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.device.dto.request.RegisterDeviceRequest;
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

                // 이미 존재하는 토큰인지 확인
                deviceRepository.findByToken(request.deviceToken()).ifPresent(existingDevice -> {
                        if (!existingDevice.getUserId().equals(userId)) {
                                // 다른 유저가 해당 토큰을 가지고 있는 경우
                                userRepository.findById(existingDevice.getUserId()).ifPresent(preOwner -> {
                                        preOwner.updateDevice(null);
                                        userRepository.save(preOwner);
                                });

                                // 기존 디바이스 삭제
                                deviceRepository.delete(existingDevice);
                                deviceRepository.flush();
                        }
                });

                Device device = deviceRepository.findByUserId(userId)
                                .orElseGet(() -> Device.builder()
                                                .token(request.deviceToken())
                                                .osType(request.osType())
                                                .userId(userId).build());

                device.updateDevice(request.deviceToken(), request.osType());
                deviceRepository.save(device);

                user.updateDevice(device);
        }

}
