package com.umc9th.areumdap.domain.chatbot.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorySummaryPromptBuilder {
  private static final ObjectMapper om = JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .build();

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static String systemPrompt;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("prompts/chatbot-system-prompt.txt");
        systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
    }

  public static String build(List<UserChatThread> chatThreads) {
    StringBuilder summaryBlock = new StringBuilder();

    chatThreads.forEach(thread -> {
      String summary = thread.getChatReport().getSummaryContent();

      if (summary == null || summary.isBlank()) {
        return;
      }

      summaryBlock
          .append("- ")
          .append(thread.getUpdatedAt().format(DATE_FORMATTER))
          .append(": ")
          .append(summary)
          .append("\n");
    });

    return systemPrompt.replace(
        "{{session_summary}}",
        summaryBlock);
  }
}
