package com.umc9th.areumdap.domain.auth.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "email_verification")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailVerification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50, unique = true)
    private String email;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name ="verification_code", length = 6)
    private String verificationCode;

    public void updateVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void updateIsVerified(boolean verified) {
        this.verified = verified;
    }

}
