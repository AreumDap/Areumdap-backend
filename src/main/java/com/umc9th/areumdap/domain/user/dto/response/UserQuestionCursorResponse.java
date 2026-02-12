package com.umc9th.areumdap.domain.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserQuestionCursorResponse(
        long totalCount,                       // 태그 기준 전체 개수
        List<UserQuestionResponse> questions,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static UserQuestionCursorResponse of(
            long totalCount,
            List<UserQuestionResponse> questions,
            LocalDateTime nextCursorTime,
            Long nextCursorId,
            boolean hasNext
    ) {
        return new UserQuestionCursorResponse(
                totalCount,
                questions,
                nextCursorTime,
                nextCursorId,
                hasNext
        );
    }

    public static UserQuestionCursorResponse empty(long totalCount) {
        return new UserQuestionCursorResponse(
                totalCount,
                List.of(),
                null,
                null,
                false
        );
    }

}
