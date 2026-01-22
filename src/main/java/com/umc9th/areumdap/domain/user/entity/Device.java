package com.umc9th.areumdap.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "device")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 512)
    private String token;

    @Column(name = "os_type", length = 512)
    private String osType;

    public void updateDevice(String deviceToken, String osType) {
        this.token = deviceToken;
        this.osType = osType;
    }
}
