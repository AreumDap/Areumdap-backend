package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatThreadRepository extends JpaRepository<UserChatThread, Long> {
}
