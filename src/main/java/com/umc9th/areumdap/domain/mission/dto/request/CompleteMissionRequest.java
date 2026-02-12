package com.umc9th.areumdap.domain.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CompleteMissionRequest(
        @NotNull(message = "missionId는 필수입니다")
        @Schema(description = "완료할 미션 ID", example = "1")
        Long missionId
) {
}
