package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {}