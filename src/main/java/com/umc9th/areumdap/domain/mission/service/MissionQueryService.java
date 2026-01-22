package com.umc9th.areumdap.domain.mission.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.mission.dto.response.MissionResponse;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionQueryService {

    private final MissionRepository missionRepository;

    public MissionResponse getMissionDetail(Long missionId, Long userId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MISSION_NOT_FOUND));

        if (!mission.getUserChatThread().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.MISSION_FORBIDDEN);
        }

        return MissionResponse.from(mission);
    }
}
