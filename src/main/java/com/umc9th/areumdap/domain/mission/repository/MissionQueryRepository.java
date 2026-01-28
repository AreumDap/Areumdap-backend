package com.umc9th.areumdap.domain.mission.repository;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionQueryRepository extends JpaRepository<Mission, Long> {
    List<Mission> findAllByUserChatThread_User_Id(Long userId);

    @Query("SELECT m FROM Mission m JOIN FETCH m.userChatThread uct JOIN FETCH uct.user WHERE m.id = :id")
    Optional<Mission> findByIdWithUser(@Param("id") Long id);

    // 첫 페이지 (tag 없음)
    @Query("""
        SELECT m
        FROM Mission m
        WHERE m.userChatThread.user.id = :userId
          AND m.missionStatus = :status
        ORDER BY m.updatedAt DESC, m.id DESC
    """)
    List<Mission> findCompletedFirstPage(
            @Param("userId") Long userId,
            @Param("status") MissionStatus status,
            Pageable pageable
    );

    // 첫 페이지 (tag 있음)
    @Query("""
        SELECT m
        FROM Mission m
        WHERE m.userChatThread.user.id = :userId
          AND m.missionStatus = :status
          AND m.tag = :tag
        ORDER BY m.updatedAt DESC, m.id DESC
    """)
    List<Mission> findCompletedFirstPageWithTag(
            @Param("userId") Long userId,
            @Param("status") MissionStatus status,
            @Param("tag") Tag tag,
            Pageable pageable
    );

    // 커서 이후 (tag 없음)
    @Query("""
        SELECT m
        FROM Mission m
        WHERE m.userChatThread.user.id = :userId
          AND m.missionStatus = :status
          AND (
                m.updatedAt < :cursorTime
             OR (m.updatedAt = :cursorTime AND m.id < :cursorId)
          )
        ORDER BY m.updatedAt DESC, m.id DESC
    """)
    List<Mission> findCompletedWithCursor(
            @Param("userId") Long userId,
            @Param("status") MissionStatus status,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    // 커서 이후 (tag 있음)
    @Query("""
        SELECT m
        FROM Mission m
        WHERE m.userChatThread.user.id = :userId
          AND m.missionStatus = :status
          AND m.tag = :tag
          AND (
                m.updatedAt < :cursorTime
             OR (m.updatedAt = :cursorTime AND m.id < :cursorId)
          )
        ORDER BY m.updatedAt DESC, m.id DESC
    """)
    List<Mission> findCompletedWithCursorAndTag(
            @Param("userId") Long userId,
            @Param("status") MissionStatus status,
            @Param("tag") Tag tag,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}


