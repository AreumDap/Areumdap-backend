package com.umc9th.areumdap.domain.chatbot.dto.response;

import java.util.List;

public record SelfPracticesResponse(
        List<SelfPractice> selfPractices
) {
    public record SelfPractice(
            String tag,
            String title,
            String content,
            String tip,
            String duration,
            Long reward
    ) {
    }
}
