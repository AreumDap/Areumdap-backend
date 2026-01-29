package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findByUserChatThreadOrderByCreatedAtAsc(UserChatThread userChatThread);

    List<ChatHistory> findByUserChatThreadIdOrderByCreatedAtAsc(Long threadId);

}
