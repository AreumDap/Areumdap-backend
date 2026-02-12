package com.umc9th.areumdap.domain.mission.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateMissionRequest(
        @NotNull Long userChatThreadId
) {
}
