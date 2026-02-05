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

                // 1. 요청된 토큰을 가진 기기와 현재 유저의 기존 기기 조회
                java.util.Optional<Device> deviceByToken = deviceRepository.findByToken(request.deviceToken());
                java.util.Optional<Device> deviceByUser = deviceRepository.findByUserId(userId);

                Device targetDevice;

                if (deviceByToken.isPresent()) {
                        targetDevice = deviceByToken.get();
                        // 2. 해당 토큰의 주인이 내가 아니면 -> 주인 변경 (기기, 토큰 재활용)
                        if (!targetDevice.getUserId().equals(userId)) {
                                // 이전 주인의 FK 참조 제거 (필수)
                                userRepository.findById(targetDevice.getUserId()).ifPresent(preOwner -> {
                                        preOwner.updateDevice(null);
                                        userRepository.saveAndFlush(preOwner);
                                });
                                // 주인 변경
                                targetDevice.changeUserId(userId);
                        }

                        // 3. 만약 내가 기존에 다른 기기(다른 토큰)를 가지고 있었다면 -> 1인 1기기 정책 유지 위해 삭제 -> 삭제 로직 제거
                        // if (deviceByUser.isPresent() &&
                        // !deviceByUser.get().getId().equals(targetDevice.getId())) {
                        // deviceRepository.delete(deviceByUser.get());
                        // }

                } else if (deviceByUser.isPresent()) {
                        // 4. 토큰은 새로운데, 나는 이미 기기가 있음 -> 내 기기 정보(토큰) 업데이트하여 재활용
                        targetDevice = deviceByUser.get();
                } else {
                        // 5. 완전히 새로운 기기 등록
                        targetDevice = Device.builder()
                                        .userId(userId)
                                        .token(request.deviceToken())
                                        .osType(request.osType())
                                        .build();
                }

                // 6. 최신 정보 반영 및 저장
                targetDevice.updateDevice(request.deviceToken(), request.osType());
                deviceRepository.save(targetDevice);

                user.updateDevice(targetDevice);
        }

}
