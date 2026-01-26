package com.umc9th.areumdap.domain.chatbot.service;

import org.springframework.ai.chat.prompt.Prompt;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class ChatbotAiService {
    private final OpenAiChatClient chatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private String systemPrompt;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("prompts/chatbot-system-prompt.txt");
        this.systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
    }
    public String generateResponse(UserChatThread chatThread, String userMessage){
        List<ChatHistory> histories = chatHistoryRepository
                .findByUserChatThreadOrderByCreatedAtAsc(chatThread);

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));

        for (ChatHistory history : histories) {
            if (history.getSenderType() == SenderType.BOT) {
                messages.add(new AssistantMessage(history.getContent()));
            } else {
                messages.add(new UserMessage(history.getContent()));
            }
        }

        messages.add(new UserMessage(userMessage));

        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }
}
