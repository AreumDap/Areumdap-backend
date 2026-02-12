package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryQueryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByUserChatThreadIdOrderByCreatedAtAsc(Long threadId);

}
