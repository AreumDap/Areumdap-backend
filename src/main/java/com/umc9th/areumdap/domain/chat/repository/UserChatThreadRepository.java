package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatThreadRepository extends JpaRepository<UserChatThread, Long> {
    List<UserChatThread> findByUserId(Long userId);

    Optional<UserChatThread> findByIdAndUser_IdAndUser_DeletedFalse(
            Long threadId,
            Long userId
    );
}
