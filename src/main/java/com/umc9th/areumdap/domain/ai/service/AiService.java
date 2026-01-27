package com.umc9th.areumdap.domain.ai.service;

import com.umc9th.areumdap.domain.ai.builder.HistorySummaryPromptBuilder;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import com.umc9th.areumdap.domain.chat.service.UserChatThreadQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    private final ChatClient chatClient;
    private UserChatThreadQueryService userChatThreadQueryService;

    public String getHistorySummary(Long userId) {
        List<UserChatThread> userChatThreads = userChatThreadQueryService.findByUserId(userId);
        String prompt = HistorySummaryPromptBuilder.build(userChatThreads);

        String result = chatClient.call(prompt);

        log.debug(result);

        return result;
    }
}
