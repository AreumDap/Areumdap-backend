package com.umc9th.areumdap.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "devices_tokens")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_token", length = 512)
    private String deviceToken;

    @Column(name = "os_type", length = 512)
    private String osType;

    public void updateDeviceToken(String deviceToken, String osType) {
        this.deviceToken = deviceToken;
        this.osType = osType;
    }
}
