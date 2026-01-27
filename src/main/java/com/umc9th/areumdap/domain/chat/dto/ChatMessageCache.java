package com.umc9th.areumdap.domain.chat.dto;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.enums.SenderType;

import java.io.Serializable;

public record ChatMessageCache(
        String content,
        SenderType senderType
) implements Serializable {

    public static ChatMessageCache from(ChatHistory chatHistory) {
        return new ChatMessageCache(
                chatHistory.getContent(),
                chatHistory.getSenderType()
        );
    }
}