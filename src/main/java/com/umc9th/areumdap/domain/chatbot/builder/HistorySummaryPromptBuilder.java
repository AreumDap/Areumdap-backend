package com.umc9th.areumdap.domain.chatbot.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
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

      if (chatReport == null || chatReport.getSummaryContent().isBlank()) {
        return;
      }

      summaryBlock
          .append("- ")
          .append(thread.getUpdatedAt().format(DATE_FORMATTER))
          .append(": ")
          .append(chatReport.getSummaryContent())
          .append("\n");
    });

    return SYSTEM_PROMPT.replace(
        "{{session_summary}}",
        summaryBlock);
  }
}
