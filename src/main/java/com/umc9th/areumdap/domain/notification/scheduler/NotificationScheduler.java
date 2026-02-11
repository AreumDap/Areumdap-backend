package com.umc9th.areumdap.domain.notification.scheduler;

import com.umc9th.areumdap.domain.notification.dto.response.NotificationTargetUser;
import com.umc9th.areumdap.domain.notification.service.NotificationCommandService;
import com.umc9th.areumdap.domain.question.service.QuestionCommandService;
import com.umc9th.areumdap.domain.question.service.QuestionQueryService;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final UserQueryService userQueryService;
    private final NotificationCommandService notificationCommandService;
    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void sendScheduledNotifications() {
        List<NotificationTargetUser> targets =
                userQueryService.findUsersToNotify();

        targets.forEach(this::processNotification);
    }

    private void processNotification(NotificationTargetUser target) {
        if (target.deviceToken() == null)
            return;

        try {
            UserQuestion question = resolveTodayQuestion(target.userId());
            Map<String, String> data = Map.of("content", question.getContent());
            log.info("푸시 알림 전송 userId={}", target.userId());
            notificationCommandService.sendPushAlarm(
                    target.deviceToken(),
                    "아름답",
                    question.getContent(),
                    data
            );
        } catch (Exception e) {
            log.error("알림 전송 실패 userId={}", target.userId(), e);
        }
    }

    private UserQuestion resolveTodayQuestion(Long userId) {
        List<UserQuestion> todayQuestions = questionQueryService.getTodayQuestions(userId);
        if (!todayQuestions.isEmpty())
            return todayQuestions.get(0);

        List<UserQuestion> newQuestions = questionCommandService.assignRandomQuestions(userId);
        if (newQuestions.isEmpty())
            throw new IllegalStateException("질문 생성 실패 userId=" + userId);

        return newQuestions.get(0);
    }

}
