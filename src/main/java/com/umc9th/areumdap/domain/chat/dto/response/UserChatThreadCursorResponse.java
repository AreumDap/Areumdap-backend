package com.umc9th.areumdap.domain.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserChatThreadCursorResponse(
        List<GetUserChatThreadResponse> userChatThreads,
        LocalDateTime nextCursorTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static UserChatThreadCursorResponse empty() {
        return new UserChatThreadCursorResponse(
                List.of(),
                null,
                null,
                false
        );
    }
}
