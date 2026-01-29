package com.umc9th.areumdap.domain.chat.dto.response;

import java.util.List;

public record GetChatHistoriesResponse(
        Long threadId,
        List<ChatHistoryResponse> histories
) {
    public static GetChatHistoriesResponse of(
            Long threadId,
            List<ChatHistoryResponse> histories
    ) {
        return new GetChatHistoriesResponse(threadId, histories);
    }
}
