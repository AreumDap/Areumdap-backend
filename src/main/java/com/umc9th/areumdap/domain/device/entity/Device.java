package com.umc9th.areumdap.domain.device.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "device")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 512, nullable = false)
    private String token;

    @Column(name = "os_type", length = 512, nullable = false)
    private String osType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public void updateDevice(String deviceToken, String osType) {
        this.token = deviceToken;
        this.osType = osType;
    }

}
