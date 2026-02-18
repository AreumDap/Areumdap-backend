package com.umc9th.areumdap.domain.chatbot.builder;

import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorySummaryPromptBuilder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String SYSTEM_PROMPT;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/history-system-prompt.txt");
            SYSTEM_PROMPT = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("history-summary-system-prompt 로드 실패", e);
        }
    }

    public static String build(List<UserChatThread> chatThreads) {
        StringBuilder summaryBlock = new StringBuilder();

        chatThreads.forEach(thread -> {
            ChatReport chatReport = thread.getChatReport();
            String summary = chatReport != null ? chatReport.getSummaryContent() : null;
            if (summary == null || summary.isBlank())
                return;

            summaryBlock
                    .append("- ")
                    .append(thread.getUpdatedAt().format(DATE_FORMATTER))
                    .append(": ")
                    .append(summary)
                    .append("\n");
        });

        if(summaryBlock.isEmpty()) {
            return null;
        }

        return SYSTEM_PROMPT.replace(
                "{{session_summary}}",
                summaryBlock);
    }

}
