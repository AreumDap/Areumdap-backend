package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.enums.SenderType;

import java.time.LocalDateTime;

public record ChatHistoryResponse(

        Long id,
        String content,
        SenderType senderType,
        LocalDateTime createdAt

) {
    public static ChatHistoryResponse from(ChatHistory history) {
        return new ChatHistoryResponse(
                history.getId(),
                history.getContent(),
                history.getSenderType(),
                history.getCreatedAt()
        );
    }
}
