package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserChatThreadQueryRepository extends Repository<UserChatThread, Long> {

    // 최신순 (첫 페이지)
    @Query("""
        select t
        from UserChatThread t
        where t.user.id = :userId
        order by t.updatedAt desc, t.id desc
    """)
    List<UserChatThread> findLatest(
            Long userId,
            Pageable pageable
    );

    // 최신순 (커서)
    @Query("""
        select t
        from UserChatThread t
        where t.user.id = :userId
          and (
            t.updatedAt < :cursorTime
            or (t.updatedAt = :cursorTime and t.id < :cursorId)
          )
        order by t.updatedAt desc, t.id desc
    """)
    List<UserChatThread> findLatestWithCursor(
            Long userId,
            LocalDateTime cursorTime,
            Long cursorId,
            Pageable pageable
    );

    // 즐겨찾기 최신순 (첫 페이지)
    @Query("""
        select t
        from UserChatThread t
        where t.user.id = :userId
          and t.favorite = true
        order by t.updatedAt desc, t.id desc
    """)
    List<UserChatThread> findFavoriteLatest(
            Long userId,
            Pageable pageable
    );

    // 즐겨찾기 최신순 (커서)
    @Query("""
        select t
        from UserChatThread t
        where t.user.id = :userId
          and t.favorite = true
          and (
            t.updatedAt < :cursorTime
            or (t.updatedAt = :cursorTime and t.id < :cursorId)
          )
        order by t.updatedAt desc, t.id desc
    """)
    List<UserChatThread> findFavoriteLatestWithCursor(
            Long userId,
            LocalDateTime cursorTime,
            Long cursorId,
            Pageable pageable
    );
}
