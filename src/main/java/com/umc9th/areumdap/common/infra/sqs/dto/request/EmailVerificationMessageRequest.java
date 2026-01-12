package com.umc9th.areumdap.common.infra.sqs.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationMessageRequest(
        @NotBlank String email,
        @NotBlank String verificationCode
) {
}
