package com.umc9th.areumdap.domain.question.dto.response;

import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import com.umc9th.areumdap.domain.question.entity.UserQuestion;

import java.time.LocalDateTime;
import java.util.List;

public record UserQuestionResponse(
        Long questionId,
        String content,
        Tag tag,
        LocalDateTime createdAt
) {

    public static UserQuestionResponse from(UserQuestion userQuestion) {
        QuestionBank qb = userQuestion.getQuestionBank();

        return new UserQuestionResponse(
                qb.getId(),
                qb.getContent(),
                qb.getTag(),
                userQuestion.getCreatedAt() // 저장 시점 기준
        );
    }

    public static List<UserQuestionResponse> fromUserQuestions(
            List<UserQuestion> userQuestions
    ) {
        return userQuestions.stream()
                .map(UserQuestionResponse::from)
                .toList();
    }
}
