package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion,Long> {

    boolean existsByUser_IdAndChatHistory_Id(Long userId, Long chatHistoryId);

}
