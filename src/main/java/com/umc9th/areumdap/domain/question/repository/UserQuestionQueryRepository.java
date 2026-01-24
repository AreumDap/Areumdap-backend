package com.umc9th.areumdap.domain.question.repository;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQuestionQueryRepository extends JpaRepository<UserQuestion, Long> { //당일에 랜덤 배정된 질문인지 확인하는 쿼리
    @Query(value = "SELECT * FROM user_question WHERE user_id = :userId AND created_at BETWEEN :start AND :end ORDER BY created_at DESC LIMIT 5", nativeQuery = true)
    List<UserQuestion> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId,
                                                                @Param("start") LocalDateTime start,
                                                                @Param("end") LocalDateTime end
    );
}
