package com.umc9th.areumdap.domain.chatbot.builder;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MissionRewardPromptBuilder {

    private static final String SYSTEM_PROMPT;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/chatbot-reward-system-prompt.txt");
            SYSTEM_PROMPT = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("chatbot-reward-system-prompt.txt 로드 실패", e);
        }
    }

    public static String build(String summary) {
        return SYSTEM_PROMPT + "\n\n[대화 요약]\n\n" + summary;
    }
}
