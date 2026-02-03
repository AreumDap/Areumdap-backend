package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQuestionQueryRepository extends JpaRepository<UserQuestion, Long> {

    @Query("""
        SELECT uq
        FROM UserQuestion uq
        WHERE uq.user.id = :userId
        ORDER BY uq.createdAt DESC, uq.id DESC
    """)
    List<UserQuestion> findFirstPage(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
        SELECT uq
        FROM UserQuestion uq
        WHERE uq.user.id = :userId
          AND uq.questionBank.tag = :tag
        ORDER BY uq.createdAt DESC, uq.id DESC
    """)
    List<UserQuestion> findFirstPageWithTag(
            @Param("userId") Long userId,
            @Param("tag") Tag tag,
            Pageable pageable
    );

    @Query("""
        SELECT uq
        FROM UserQuestion uq
        WHERE uq.user.id = :userId
          AND (
                uq.createdAt < :cursorTime
             OR (uq.createdAt = :cursorTime AND uq.id < :cursorId)
          )
        ORDER BY uq.createdAt DESC, uq.id DESC
    """)
    List<UserQuestion> findWithCursor(
            @Param("userId") Long userId,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
        SELECT uq
        FROM UserQuestion uq
        WHERE uq.user.id = :userId
          AND uq.questionBank.tag = :tag
          AND (
                uq.createdAt < :cursorTime
             OR (uq.createdAt = :cursorTime AND uq.id < :cursorId)
          )
        ORDER BY uq.createdAt DESC, uq.id DESC
    """)
    List<UserQuestion> findWithCursorAndTag(
            @Param("userId") Long userId,
            @Param("tag") Tag tag,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("SELECT uq FROM UserQuestion uq " +
            "LEFT JOIN FETCH uq.questionBank " +
            "WHERE uq.user.id = :userId " +
            "AND uq.createdAt BETWEEN :start AND :end " +
            "ORDER BY uq.createdAt DESC")
    List<UserQuestion> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable // service에서 개수 설정
    );

    // tag 없는 경우
    @Query("""
    SELECT COUNT(uq)
    FROM UserQuestion uq
    WHERE uq.user.id = :userId
""")
    long countByUserId(@Param("userId") Long userId);


    // tag 있는 경우
    @Query("""
    SELECT COUNT(uq)
    FROM UserQuestion uq
    JOIN uq.questionBank qb
    WHERE uq.user.id = :userId
      AND qb.tag = :tag
""")
    long countByUserIdAndTagOnly(
            @Param("userId") Long userId,
            @Param("tag") Tag tag
    );
}

