package com.umc9th.areumdap.domain.question.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveUnusedQuestionScheduler {
    private final JobLauncher jobLauncher;
    private final Job removeUnusedQuestionsJob;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void run() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(removeUnusedQuestionsJob, params);
            log.info("미사용 질문 제거 배치 실행 완료");
        } catch (Exception e) {
            log.error("미사용 질문 제거 배치 실행 실패", e);
        }
    }
}
