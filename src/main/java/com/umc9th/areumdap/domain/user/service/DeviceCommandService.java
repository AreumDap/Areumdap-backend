package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.dto.request.UpdateDeviceRequest;
import com.umc9th.areumdap.domain.user.entity.Device;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.DeviceRepository;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceCommandService {
    private final DeviceRepository deviceRepository;

    public void updateDevice(User user, UpdateDeviceRequest dto) {
        Device device = user.getDevice();
        if (device == null) {
            Device newDevice = Device.builder()
                    .token(dto.deviceToken())
                    .osType(dto.osType())
                    .build();

            deviceRepository.save(newDevice);

            user.updateDeviceToken(newDevice);
            return;
        }
        device.updateDevice(dto.deviceToken(), dto.osType());
    }
}
