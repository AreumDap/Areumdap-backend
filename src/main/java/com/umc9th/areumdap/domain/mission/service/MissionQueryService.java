package com.umc9th.areumdap.domain.mission.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.mission.dto.response.GetMissionResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionCursorResponse;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.mission.repository.MissionQueryRepository;
import com.umc9th.areumdap.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.umc9th.areumdap.domain.mission.enums.MissionStatus.COMPLETED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionQueryService {

    private final MissionQueryRepository missionQueryRepository;
    private final MissionRepository missionRepository;

    // 완료된 미션 목록 커서 기반 조회
    public MissionCursorResponse getCompletedMissionsWithCursor(
            Long userId,
            CursorRequest request
    ) {
        int size = request.size();

        // cursorTime / cursorId 짝 검증
        if ((request.cursorTime() == null) != (request.cursorId() == null)) {
            throw new GeneralException(ErrorStatus.CURSOR_BAD_REQUEST);
        }

        // OffsetDateTime → LocalDateTime 변환
        LocalDateTime cursorTime = request.cursorTime() == null
                ? null
                : request.cursorTime().toLocalDateTime();

        Pageable pageable = PageRequest.of(0, size + 1);

        long totalCount = (request.tag() == null)
                ? missionQueryRepository.countCompletedByUser(
                userId, COMPLETED
        )
                : missionQueryRepository.countCompletedByUserAndTag(
                userId, COMPLETED, request.tag()
        );

        List<Mission> missions = fetchMissions(
                userId,
                request.tag(),
                cursorTime,
                request.cursorId(),
                pageable
        );

        if (missions.isEmpty()) {
            return MissionCursorResponse.empty(totalCount);
        }

        boolean hasNext = missions.size() > size;


        List<Mission> sliced = hasNext
                ? missions.subList(0, size)
                : missions;

        Mission last = sliced.get(sliced.size() - 1);

        return MissionCursorResponse.of(
                totalCount,
                GetMissionResponse.from(sliced),
                last.getUpdatedAt(),
                last.getId(),
                hasNext
        );
    }


    //커서 존재 여부 / tag 여부에 따라 Repository 분기
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

    public MissionResponse getMissionDetail(Long missionId, Long userId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MISSION_NOT_FOUND));

        if (!mission.getUserChatThread().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.MISSION_FORBIDDEN);
        }

        return MissionResponse.from(mission);
    }

}
