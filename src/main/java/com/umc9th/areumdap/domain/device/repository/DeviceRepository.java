package com.umc9th.areumdap.domain.device.repository;

import com.umc9th.areumdap.domain.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUserId(Long userId);
}