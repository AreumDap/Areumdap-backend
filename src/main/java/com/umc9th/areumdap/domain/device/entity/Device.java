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

    @Column(name = "token", length = 512, nullable = false, unique = true)
    private String token;

    @Column(name = "os_type", length = 512, nullable = false)
    private String osType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 디바이스 정보 업데이트
    public void updateDevice(Long userId, String osType) {
        this.userId = userId;
        this.osType = osType;
    }

    // 디바이스 객체 생성
    public static Device create(Long userId, String token, String osType) {
        return Device.builder()
                .userId(userId)
                .token(token)
                .osType(osType)
                .build();
    }

}
