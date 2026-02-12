package com.umc9th.areumdap.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record GetChatHistoriesResponse(
        Long threadId,
        Long reportId,
        List<ChatHistoryResponse> histories
) {

    public static GetChatHistoriesResponse of(
            Long threadId,
            Long reportId,
            List<ChatHistoryResponse> histories
    ) {
        return new GetChatHistoriesResponse(threadId, reportId, histories);
    }
}
