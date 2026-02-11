package com.umc9th.areumdap.domain.device.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.device.dto.request.RegisterDeviceRequest;
import com.umc9th.areumdap.domain.device.entity.Device;
import com.umc9th.areumdap.domain.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceCommandService {

    private final DeviceRepository deviceRepository;

    // 디바이스 정보 등록
    public void registerDevice(Long userId, RegisterDeviceRequest request) {
        Optional<Device> optionalDevice = deviceRepository.findByToken(request.deviceToken());

        // 토큰 최초 등록인 경우
        if (optionalDevice.isEmpty()) {
            try {
                Device device = Device.create(userId, request.deviceToken(), request.osType());
                deviceRepository.save(device);
            } catch (DataIntegrityViolationException e) {
                Device device = deviceRepository.findByToken(request.deviceToken())
                        .orElseThrow(() ->new GeneralException(ErrorStatus.DEVICE_TOKEN_CONFLICT));
                device.updateDevice(userId, request.osType());
            }
        } else {
            Device device = optionalDevice.get();
            device.updateDevice(userId, request.osType());
        }
    }

}
