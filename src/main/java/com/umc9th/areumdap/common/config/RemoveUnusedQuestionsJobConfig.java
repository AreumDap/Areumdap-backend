package com.umc9th.areumdap.common.config;

import com.umc9th.areumdap.domain.user.repository.UserQuestionRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class RemoveUnusedQuestionsJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserQuestionRepository userQuestionRepository;

    @Bean
    public Job removeUnusedQuestionsJob(){
        return new JobBuilder("removeUnusedQuestionsJob", jobRepository)
                .start(removeUnusedQuestionsStep())
                .build();
    }

    @Bean
    public Step removeUnusedQuestionsStep() {
        return new StepBuilder("removeUnusedQuestionsStep", jobRepository)
                .tasklet(removeUnusedQuestionsTasklet(), transactionManager)
                .build();
    }
    @Bean
    public Tasklet removeUnusedQuestionsTasklet() {
        return (contribution, chunkContext) -> {
            int deletedCount = userQuestionRepository.deleteAllUnused();
            log.info("미사용 질문 {}건 삭제 완료", deletedCount);
            return RepeatStatus.FINISHED;
        };
    }
}
