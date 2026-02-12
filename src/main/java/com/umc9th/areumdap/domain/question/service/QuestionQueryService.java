package com.umc9th.areumdap.domain.question.service;

import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserQuestionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionQueryService {

    private final UserQuestionQueryRepository userQuestionQueryRepository;

    public List<UserQuestion> getTodayQuestions(Long userId){ //오늘배정된 질문 조회
        LocalDateTime start = LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay();
        LocalDateTime end = LocalDate.now(ZoneId.of("Asia/Seoul")).atTime(LocalTime.MAX);

        return userQuestionQueryRepository.findByUserIdAndCreatedAtBetween(userId, start, end, PageRequest.of(0, 5));
    }

    public List<UserQuestion> getAssignedQuestions(Long userId) { // 미사용 배정 질문 조회
        return userQuestionQueryRepository.findAssignedQuestions(userId);
    }

}
