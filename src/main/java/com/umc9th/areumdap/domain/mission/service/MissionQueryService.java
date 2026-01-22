package com.umc9th.areumdap.domain.mission.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.dto.response.CursorResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.mission.repository.MissionQueryRepository;
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
public class MissionQueryService {

    private static final MissionStatus COMPLETED = MissionStatus.COMPLETED;

    private final MissionQueryRepository missionQueryRepository;

    public CursorResponse getCompletedMissionsWithCursor(
            Long userId,
            CursorRequest request
    ) {
        int size = request.size(); // 로컬 변수로 추출

        if ((request.cursorTime() == null) != (request.cursorId() == null)) {
            throw new GeneralException(ErrorStatus.CURSOR_BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(0, size + 1);

        List<Mission> missions = fetchMissions(
                userId,
                request.tag(),
                request.cursorTime(),
                request.cursorId(),
                pageable
        );

        if (missions.isEmpty()) {
            return CursorResponse.empty();
        }

        boolean hasNext = missions.size() > size;

        List<Mission> sliced = hasNext
                ? missions.subList(0, size)
                : missions;

        Mission last = sliced.get(sliced.size() - 1);

        return new CursorResponse(
                MissionResponse.from(sliced),
                last.getUpdatedAt(),
                last.getId(),
                hasNext
        );
    }

    private List<Mission> fetchMissions(
            Long userId,
            Tag tag,
            LocalDateTime cursorTime,
            Long cursorId,
            Pageable pageable
    ) {
        // 첫 페이지
        if (cursorTime == null) {
            if (tag == null) {
                return missionQueryRepository.findCompletedFirstPage(
                        userId, COMPLETED, pageable
                );
            }
            return missionQueryRepository.findCompletedFirstPageWithTag(
                    userId, COMPLETED, tag, pageable
            );
        }

        // 커서 이후
        if (tag == null) {
            return missionQueryRepository.findCompletedWithCursor(
                    userId, COMPLETED, cursorTime, cursorId, pageable
            );
        }

        return missionQueryRepository.findCompletedWithCursorAndTag(
                userId, COMPLETED, tag, cursorTime, cursorId, pageable
        );
    }
}
