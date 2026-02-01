package com.umc9th.areumdap.domain.notification.scheduler;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.notification.service.NotificationService;
import com.umc9th.areumdap.domain.question.service.QuestionCommandService;
import com.umc9th.areumdap.domain.question.service.QuestionQueryService;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void sendScheduledNotifications() {
        LocalTime now = LocalTime
                .now(ZoneId.of("Asia/Seoul"))
                .withSecond(0)
                .withNano(0);

        List<User> users = userRepository.findUsersToNotify(now);
        Map<String, String> data = new HashMap<>();

        data.put("type", "daily");
        data.put("message", "아름답고 미운새~");

        for (User user : users) {
            if (user.getDevice() == null || user.getDevice().getToken() == null)
                continue;

            try {
                // 오늘 배정된 질문 확인
                List<UserQuestion> todayQuestions = questionQueryService.getTodayQuestions(user.getId());
                UserQuestion targetQuestion;

                if (todayQuestions.isEmpty()) {
                    // 질문이 없으면 새로 배정
                    List<UserQuestion> newQuestions = questionCommandService.assignRandomQuestions(user.getId());
                    if (newQuestions.isEmpty()) {
                        continue;
                    }
                    targetQuestion = newQuestions.get(0);
                } else {
                    targetQuestion = todayQuestions.get(0);
                }

                notificationService.sendPushAlarm(
                        user.getDevice().getToken(),
                        "오늘의 철학",
                        targetQuestion.getContent(),
                        data);
            } catch (Exception e) {
                throw new GeneralException(ErrorStatus.NOTIFICATION_SENDING_INTERNAL_SERVER_ERROR);
            }
        }
    }

}
