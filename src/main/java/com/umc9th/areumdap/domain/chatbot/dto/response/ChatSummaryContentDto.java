package com.umc9th.areumdap.domain.chatbot.dto.response;

import java.util.List;

public record ChatSummaryContentDto(
        String title,
        String summary,
        String reflectionDepth,
        List<String> keywords,
        List<String> discoveries
) {
}
