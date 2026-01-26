package com.umc9th.areumdap.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateChatThreadRequest(
        @NotBlank(message = "content는 필수입니다.")
        String content,

        @NotNull(message = "userQuestionId는 필수입니다.")
        Long userQuestionId
) {
}
