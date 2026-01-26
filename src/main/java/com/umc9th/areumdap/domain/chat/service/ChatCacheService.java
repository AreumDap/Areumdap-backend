package com.umc9th.areumdap.domain.chat.service;

import com.umc9th.areumdap.domain.chat.dto.ChatMessageCache;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "chat:thread:";
    private static final long CACHE_TTL_MINUTES = 30;

    @SuppressWarnings("unchecked")
    public List<ChatMessageCache> getChatHistories(Long threadId) { //캐시에서 대화 내역 조회
        String key = CACHE_PREFIX + threadId;
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.debug("Cache hit for thread: {}", threadId);
                return (List<ChatMessageCache>) cached;
            }
        } catch (Exception e) {
            log.warn("Failed to get cache for thread: {}", threadId, e);
        }
        return null;
    }

    public void setChatHistories(Long threadId, List<ChatHistory> histories) { //대화 내역 캐시 저장
        String key = CACHE_PREFIX + threadId;
        try {
            List<ChatMessageCache> cacheList = histories.stream()
                    .map(ChatMessageCache::from)
                    .toList();
            redisTemplate.opsForValue().set(key, cacheList, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Cache set for thread: {}", threadId);
        } catch (Exception e) {
            log.warn("Failed to set cache for thread: {}", threadId, e);
        }
    }

    public void addMessage(Long threadId, String content, SenderType senderType) { //캐시에 새 메시지 추가
        String key = CACHE_PREFIX + threadId;
        try {
            @SuppressWarnings("unchecked")
            List<ChatMessageCache> cached = (List<ChatMessageCache>) redisTemplate.opsForValue().get(key);
            if (cached != null) {
                cached.add(new ChatMessageCache(content, senderType));
                redisTemplate.opsForValue().set(key, cached, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.debug("Message added to cache for thread: {}", threadId);
            }
        } catch (Exception e) {
            log.warn("Failed to add message to cache for thread: {}", threadId, e);
        }
    }

    public void invalidateCache(Long threadId) { //캐시 삭제
        String key = CACHE_PREFIX + threadId;
        try {
            redisTemplate.delete(key);
            log.debug("Cache invalidated for thread: {}", threadId);
        } catch (Exception e) {
            log.warn("Failed to invalidate cache for thread: {}", threadId, e);
        }
    }
}