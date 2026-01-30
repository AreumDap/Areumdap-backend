package com.umc9th.areumdap.domain.chatbot.builder;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatSummaryPromptBuilder {

    private static final String SYSTEM_PROMPT;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/chatbot-summary-system-prompt");
            SYSTEM_PROMPT = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("chatbot-summary-system-prompt 로드 실패", e);
        }
    }

    public static String build(List<ChatHistory> histories) {
        StringBuilder sb = new StringBuilder();
        sb.append(SYSTEM_PROMPT).append("\n\n[대화 내역]\n\n");
        for (ChatHistory h : histories) {
            String role = (h.getSenderType() == SenderType.BOT) ? "AI" : "사용자";
            sb.append(role).append(": ").append(h.getContent()).append("\n");
        }
        return sb.toString();
    }
}
