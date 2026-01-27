package com.umc9th.areumdap.domain.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.ai.builder.HistorySummaryPromptBuilder;
import com.umc9th.areumdap.domain.ai.dto.response.AiHistorySummaryDto;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.domain.chat.service.UserChatThreadQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiService {
    private final ChatClient chatClient;
    private final UserChatThreadQueryService userChatThreadQueryService;
    private final ObjectMapper objectMapper;

    public AiHistorySummaryDto getHistorySummary(Long userId) {
        List<UserChatThread> userChatThreads = userChatThreadQueryService.findByUserId(userId);
        String prompt = HistorySummaryPromptBuilder.build(userChatThreads);

        String raw = chatClient.call(prompt);

        return parse(raw, AiHistorySummaryDto.class);
    }

    // String(JSON) -> DTO [feat. Blue]
    private <T> T parse(String raw, Class<T> clazz) {
        try {
            String json = extractJson(raw);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_RESPONSE_NOT_PARSE);
        }
    }

    private <T> T parse(String raw, TypeReference<T> typeRef) {
        try {
            String json = extractJson(raw);
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_RESPONSE_NOT_PARSE);
        }
    }

    private String extractJson(String raw) {
        int objStart = raw.indexOf("{");
        int arrStart = raw.indexOf("[");

        int start;

        if (objStart == -1 && arrStart == -1) {
            throw new GeneralException(ErrorStatus.AI_RESPONSE_NOT_JSON);
        }

        // 둘 중 먼저 나온 쪽 선택
        if (objStart == -1) start = arrStart;
        else if (arrStart == -1) start = objStart;
        else start = Math.min(objStart, arrStart);

        char openChar = raw.charAt(start);
        char closeChar = (openChar == '{') ? '}' : ']';

        int end = raw.lastIndexOf(closeChar);
        if (end == -1 || end <= start) {
            throw new GeneralException(ErrorStatus.AI_RESPONSE_NOT_JSON);
        }

        return raw.substring(start, end + 1);
    }
}
