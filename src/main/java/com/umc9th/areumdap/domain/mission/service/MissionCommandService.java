package com.umc9th.areumdap.domain.mission.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.service.CharacterCommandService;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.repository.MissionRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionCommandService {

    private final MissionRepository missionRepository;
    private final CharacterCommandService characterCommandService;
    private final UserRepository userRepository;

    // 과제 수행 완료(XP 지급)
    public void completeMission(Long userId, Long missionId) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Mission mission = missionRepository.findByIdWithUserForUpdate(missionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MISSION_NOT_FOUND));

        if (!mission.isOwnedBy(user.getId())) {
            throw new GeneralException(ErrorStatus.MISSION_FORBIDDEN);
        }

        if (mission.getMissionStatus() == MissionStatus.COMPLETED) {
            throw new GeneralException(ErrorStatus.MISSION_ALREADY_COMPLETED);
        }

        mission.complete();
        characterCommandService.addXpIfPossible(user, Math.toIntExact(mission.getReward()));
    }
}
