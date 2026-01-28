package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatThreadRepository extends JpaRepository<UserChatThread, Long> {
    List<UserChatThread> findByUserId(Long userId);
}
