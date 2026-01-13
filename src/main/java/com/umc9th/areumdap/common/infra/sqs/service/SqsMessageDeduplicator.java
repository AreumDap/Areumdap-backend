package com.umc9th.areumdap.common.infra.sqs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SqsMessageDeduplicator {

    private final StringRedisTemplate stringRedisTemplate;
    private static final Duration TTL = Duration.ofMinutes(10);

    // Redis에 저장할 Key값 생성
    private static String key(String messageId) {
        return "sqs:processed:" + messageId;
    }

    public boolean alreadyProcessed(String messageId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key(messageId)));
    }

    public void markProcessed(String messageId) {
        stringRedisTemplate.opsForValue().set(key(messageId),"1",TTL);
    }

}


