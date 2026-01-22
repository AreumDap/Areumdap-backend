package com.umc9th.areumdap.domain.mission.dto.request;

import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.LocalDateTime;

public record CursorRequest(
        Tag tag,
        LocalDateTime cursorTime,
        Long cursorId,
        Integer size
){}
