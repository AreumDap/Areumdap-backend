package com.umc9th.areumdap.domain.chatbot.service;

import org.springframework.ai.chat.prompt.Prompt;
import com.umc9th.areumdap.domain.chat.dto.ChatMessageCache;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import com.umc9th.areumdap.domain.chat.service.ChatCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final OpenAiChatClient chatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatCacheService chatCacheService;
    private String systemPrompt;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("prompts/chatbot-system-prompt.txt");
        this.systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
    }

    public String generateResponse(UserChatThread chatThread, String userMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));

        //Redis 캐시에서 먼저 조회
        List<ChatMessageCache> cachedHistories = chatCacheService.getChatHistories(chatThread.getId());

        if (cachedHistories != null) { // 캐시 히트
            log.info("Using cached chat histories for thread: {}", chatThread.getId());
            for (ChatMessageCache cache : cachedHistories) {
                if (cache.senderType() == SenderType.BOT) {
                    messages.add(new AssistantMessage(cache.content()));
                } else {
                    messages.add(new UserMessage(cache.content()));
                }
            }
        } else {  // 캐시 미스
            log.info("Cache miss, fetching from DB for thread: {}", chatThread.getId());
            List<ChatHistory> histories = chatHistoryRepository
                    .findByUserChatThreadOrderByCreatedAtAsc(chatThread);

            for (ChatHistory history : histories) {
                if (history.getSenderType() == SenderType.BOT) {
                    messages.add(new AssistantMessage(history.getContent()));
                } else {
                    messages.add(new UserMessage(history.getContent()));
                }
            }
            chatCacheService.setChatHistories(chatThread.getId(), histories); // 캐시에 저장
        }

        messages.add(new UserMessage(userMessage)); // 현재 유저 메시지 추가

        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }
}
