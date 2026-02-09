package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.notification.dto.response.NotificationTargetUser;
import com.umc9th.areumdap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserQueryRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndDeletedFalse(Long id);

    @Query("""
        SELECT new com.umc9th.areumdap.domain.notification.dto.response.NotificationTargetUser(u.id,d.token)
        FROM User u
        JOIN Device d ON u.id = d.userId
        WHERE u.deleted = false
        AND u.notificationEnabled = true
        AND u.notificationTime = :time
    """)
    List<NotificationTargetUser> findNotificationTargets(LocalTime time);
}
