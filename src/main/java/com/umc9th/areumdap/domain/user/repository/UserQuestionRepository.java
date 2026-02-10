package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion,Long> {

    boolean existsByUser_IdAndChatHistory_Id(Long userId, Long chatHistoryId);

    List<UserQuestion> findByChatHistory_UserChatThread_Id(Long threadId);

    Optional<UserQuestion> findByIdAndUser_IdAndSavedTrue(Long id, Long userId);

    List<UserQuestion> findByParentQuestion_Id(Long parentQuestionId);

    @Modifying
    @Query("""
        DELETE FROM UserQuestion uq
        WHERE uq.used = false
          AND uq.saved = false
          AND uq.id NOT IN (
              SELECT t.userQuestion.id FROM UserChatThread t
          )
    """)
    int deleteAllUnused();
}
