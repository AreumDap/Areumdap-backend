package com.umc9th.areumdap.domain.user.dto.request;

import com.umc9th.areumdap.domain.user.enums.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterUserOnboardingRequest(
        @NotNull
        @Size(min = 1, max = 3)
        List<@NotNull Season> seasons,

        @NotNull
        @Size(max = 3)
        List<@NotBlank @Size(max = 100) String> keywords,

        @NotNull
        Long characterId,

        @NotBlank
        @Size(max = 10)
        String nickname
) {
}
