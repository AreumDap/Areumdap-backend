package com.umc9th.areumdap.domain.notification.scheduler;

import com.umc9th.areumdap.domain.notification.service.NotificationService;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void sendScheduledNotifications() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<User> users = userRepository.findUsersToNotify(now);
        Map<String, String> data = new HashMap<>();

        data.put("type", "daily");
        data.put("message", "아름답고 미운새~");

        for (User user : users) {
            // TODO: 질문 추천으로 받아오기(getDailyRecommendQuestion)
            if (user.getDevice() == null || user.getDevice().getToken() == null)
                continue;
            try {
                notificationService.sendPushAlarm(
                        user.getDevice().getToken(),
                        "오늘의 철학",
                        "내게 가장 힘들었던 경험은?",
                        data
                );
            } catch (Exception e) { }
        }
    }
}
