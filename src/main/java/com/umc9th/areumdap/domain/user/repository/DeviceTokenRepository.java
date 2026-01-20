package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {}