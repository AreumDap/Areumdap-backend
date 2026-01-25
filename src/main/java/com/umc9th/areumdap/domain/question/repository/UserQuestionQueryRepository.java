package com.umc9th.areumdap.domain.question.repository;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQuestionQueryRepository extends JpaRepository<UserQuestion, Long> { //당일에 랜덤 배정된 질문인지 확인하는 쿼리
    @Query("SELECT uq FROM UserQuestion uq " +
           "WHERE uq.user.id = :userId " +
           "AND uq.createdAt BETWEEN :start AND :end " +
           "ORDER BY uq.createdAt DESC")
    List<UserQuestion> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable // service에서 개수 설정
    );
}
