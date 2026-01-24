package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.user.dto.response.UserQuestionCursorResponse;
import com.umc9th.areumdap.domain.user.dto.response.UserQuestionResponse;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserQuestionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuestionQueryService {

    private final UserQuestionQueryRepository userQuestionQueryRepository;

    public UserQuestionCursorResponse getSavedQuestionsWithCursor(
            Long userId,
            CursorRequest request
    ) {
        int size = request.size();

        // cursorTime / cursorId 쌍 검증
        if ((request.cursorTime() == null) != (request.cursorId() == null)) {
            throw new GeneralException(ErrorStatus.CURSOR_BAD_REQUEST);
        }

        LocalDateTime cursorTime = request.cursorTime() == null
                ? null
                : request.cursorTime().toLocalDateTime();

        Pageable pageable = PageRequest.of(0, size + 1);

        List<UserQuestion> userQuestions = fetchUserQuestions(
                userId,
                request.tag(),
                cursorTime,
                request.cursorId(),
                pageable
        );

        if (userQuestions.isEmpty()) {
            return UserQuestionCursorResponse.empty();
        }

        boolean hasNext = userQuestions.size() > size;

        List<UserQuestion> sliced = hasNext
                ? userQuestions.subList(0, size)
                : userQuestions;

        UserQuestion last = sliced.get(sliced.size() - 1);

        return new UserQuestionCursorResponse(
                UserQuestionResponse.fromUserQuestions(sliced),
                last.getCreatedAt(),
                last.getId(),
                hasNext
        );
    }

    private List<UserQuestion> fetchUserQuestions(
            Long userId,
            Tag tag,
            LocalDateTime cursorTime,
            Long cursorId,
            Pageable pageable
    ) {
        // 첫 페이지
        if (cursorTime == null) {
            if (tag == null) {
                return userQuestionQueryRepository.findFirstPage(userId, pageable);
            }
            return userQuestionQueryRepository.findFirstPageWithTag(
                    userId, tag, pageable
            );
        }

        // 커서 이후
        if (tag == null) {
            return userQuestionQueryRepository.findWithCursor(
                    userId, cursorTime, cursorId, pageable
            );
        }

        return userQuestionQueryRepository.findWithCursorAndTag(
                userId, tag, cursorTime, cursorId, pageable
        );
    }
}
