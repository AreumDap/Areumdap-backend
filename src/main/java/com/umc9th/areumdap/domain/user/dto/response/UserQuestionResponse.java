package com.umc9th.areumdap.domain.user.dto.response;

import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;

import java.time.LocalDateTime;
import java.util.List;

public record UserQuestionResponse(
        Long userQuestionId,
        Long questionId,
        String content,
        Tag tag,
        LocalDateTime createdAt
) {

    public static UserQuestionResponse from(UserQuestion userQuestion) {
        return new UserQuestionResponse(
                userQuestion.getId(),
                userQuestion.getQuestionBank().getId(),
                userQuestion.getContent(),
                userQuestion.getTag(),
                userQuestion.getCreatedAt()
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
