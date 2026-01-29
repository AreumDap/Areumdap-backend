package com.umc9th.areumdap.domain.chat.service;

import java.util.List;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.dto.request.UserChatThreadCursorRequest;
import com.umc9th.areumdap.domain.chat.dto.response.GetUserChatThreadResponse;
import com.umc9th.areumdap.domain.chat.dto.response.UserChatThreadCursorResponse;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadQueryRepository;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserChatThreadQueryService {

    private final UserChatThreadQueryRepository repository;
    private final UserChatThreadRepository userChatThreadRepository;

    public List<UserChatThread> findByUserId(Long userId) {
        return this.userChatThreadRepository.findByUserId(userId);
    }

    public UserChatThreadCursorResponse getThreads(
            Long userId,
            boolean favorite,
            UserChatThreadCursorRequest cursor) {
        if ((cursor.cursorTime() == null) != (cursor.cursorId() == null)) {
            throw new GeneralException(ErrorStatus.CURSOR_BAD_REQUEST);
        }

        int size = cursor.size();
        var pageable = PageRequest.of(0, size + 1);

        LocalDateTime cursorTime = cursor.cursorTime() == null
                ? null
                : cursor.cursorTime().toLocalDateTime();

        List<UserChatThread> threads = cursorTime == null
                ? fetchFirst(userId, favorite, pageable)
                : fetchNext(userId, favorite, cursorTime, cursor.cursorId(), pageable);

        if (threads.isEmpty()) {
            return UserChatThreadCursorResponse.empty();
        }

        boolean hasNext = threads.size() > size;
        List<UserChatThread> sliced = hasNext
                ? threads.subList(0, size)
                : threads;

        UserChatThread last = sliced.get(sliced.size() - 1);

        return new UserChatThreadCursorResponse(
                GetUserChatThreadResponse.from(sliced),
                last.getUpdatedAt(),
                last.getId(),
                hasNext);
    }

    private List<UserChatThread> fetchFirst(Long userId, boolean favorite, PageRequest pageable) {
        return favorite
                ? repository.findFavoriteLatest(userId, pageable)
                : repository.findLatest(userId, pageable);
    }

    private List<UserChatThread> fetchNext(
            Long userId,
            boolean favorite,
            LocalDateTime cursorTime,
            Long cursorId,
            PageRequest pageable) {
        return favorite
                ? repository.findFavoriteLatestWithCursor(userId, cursorTime, cursorId, pageable)
                : repository.findLatestWithCursor(userId, cursorTime, cursorId, pageable);
    }
}
