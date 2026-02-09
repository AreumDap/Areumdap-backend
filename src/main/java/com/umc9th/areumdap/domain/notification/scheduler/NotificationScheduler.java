package com.umc9th.areumdap.domain.notification.scheduler;

import com.umc9th.areumdap.domain.notification.dto.response.NotificationTargetUser;
import com.umc9th.areumdap.domain.notification.service.NotificationService;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    private final UserQueryService userQueryService;
    private final NotificationService notificationService;
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

            notificationService.sendPushAlarm(
                    target.deviceToken(),
                    "오늘의 철학",
                    "오늘의 철학 질문을 시작해 보아요!",
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
