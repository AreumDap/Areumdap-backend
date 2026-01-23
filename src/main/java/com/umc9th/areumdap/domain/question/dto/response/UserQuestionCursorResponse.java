package com.umc9th.areumdap.domain.question.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserQuestionCursorResponse(
        List<UserQuestionResponse> questions,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static UserQuestionCursorResponse empty() {
        return new UserQuestionCursorResponse(
                List.of(),
                null,
                null,
                false
        );
    }
}

