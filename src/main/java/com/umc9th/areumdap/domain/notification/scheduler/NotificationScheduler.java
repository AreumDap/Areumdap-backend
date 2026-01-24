package com.umc9th.areumdap.domain.notification.scheduler;

import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void sendScheduledNotifications() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<User> users = userRepository.findUsersToNotify(now);

        for (User user : users) {
            System.out.println("Sending notifications for user: " + user.getName());
        }
    }
}
