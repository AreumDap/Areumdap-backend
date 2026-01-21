package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.dto.command.DeviceTokenCommand;
import com.umc9th.areumdap.domain.user.entity.DeviceToken;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.DeviceTokenRepository;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceTokenCommandService {
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public void updateDeviceToken(Long userId, DeviceTokenCommand dto) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DeviceToken deviceToken = user.getDeviceToken();
        if (deviceToken == null) {
            DeviceToken newDeviceToken = DeviceToken.builder()
                    .deviceToken(dto.deviceToken())
                    .osType(dto.osType())
                    .build();

            deviceTokenRepository.save(newDeviceToken);

            user.updateDeviceToken(newDeviceToken);
            return;
        }
        deviceToken.updateDeviceToken(dto.deviceToken(), dto.osType());
    }
}
