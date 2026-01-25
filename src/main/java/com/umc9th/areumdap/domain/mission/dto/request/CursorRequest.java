package com.umc9th.areumdap.domain.mission.dto.request;

import com.umc9th.areumdap.domain.mission.enums.Tag;
import jakarta.validation.constraints.Min;

import java.time.OffsetDateTime;

public record CursorRequest(
        Tag tag,
        OffsetDateTime cursorTime,
        Long cursorId,
        @Min(1)
        Integer size
){}
