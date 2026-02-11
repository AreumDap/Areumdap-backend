package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatHistoryQueryRepository extends CrudRepository<ChatHistory, Long> {
    List<ChatHistory> findByUserChatThreadIdOrderByCreatedAtAsc(Long threadId);

}
