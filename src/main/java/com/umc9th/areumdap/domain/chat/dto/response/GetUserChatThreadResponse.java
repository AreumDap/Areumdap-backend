package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.mission.enums.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record GetUserChatThreadResponse(
        Long threadId,
        Tag tag,
        String content,
        LocalDateTime createdAt,
        String summary,
        boolean favorite
) {
    public static GetUserChatThreadResponse from(UserChatThread thread) {
        if (thread.getUserQuestion() == null){
            throw new GeneralException(ErrorStatus.USER_QUESTION_NOT_FOUND);
        }
        return new GetUserChatThreadResponse(
                thread.getId(),
                thread.getUserQuestion().getTag(),
                thread.getUserQuestion().getContent(),
                thread.getCreatedAt(),
                thread.getSummary(),
                thread.isFavorite()
        );
    }

    public static List<GetUserChatThreadResponse> from(List<UserChatThread> threads) {
        return threads.stream()
                .map(GetUserChatThreadResponse::from)
                .toList();
    }
}
