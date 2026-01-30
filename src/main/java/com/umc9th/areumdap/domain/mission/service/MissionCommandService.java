package com.umc9th.areumdap.domain.mission.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.service.CharacterCommandService;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import com.umc9th.areumdap.domain.chatbot.dto.response.SelfPracticesResponse;
import com.umc9th.areumdap.domain.chatbot.dto.response.SelfPracticesResponse.SelfPractice;
import com.umc9th.areumdap.domain.chatbot.service.ChatbotService;
import com.umc9th.areumdap.domain.mission.dto.response.CreateMissionResponse;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.mission.repository.MissionRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionCommandService {

    private final MissionRepository missionRepository;
    private final CharacterCommandService characterCommandService;
    private final UserRepository userRepository;
    private final UserChatThreadRepository userChatThreadRepository;
    private final ChatbotService chatbotService;
    private final TransactionTemplate transactionTemplate;

    public CreateMissionResponse createMissions(Long userId, Long userChatThreadId) {
        // 트랜잭션 1: 스레드 조회 + 권한 확인 + 요약 가져오기
        UserChatThread chatThread = transactionTemplate.execute(status -> {
            User user = userRepository.findByIdAndDeletedFalse(userId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

            UserChatThread thread = userChatThreadRepository.findById(userChatThreadId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND));

            if (!thread.getUser().getId().equals(userId)) {
                throw new GeneralException(ErrorStatus.CHAT_THREAD_ACCESS_DENIED);
            }

            if (thread.getSummary() == null || thread.getSummary().isBlank()) {
                throw new GeneralException(ErrorStatus.SUMMARY_NOT_FOUND);
            }

            return thread;
        });

        // AI 미션 생성 (트랜잭션 외부 - DB 커넥션 점유 X)
        SelfPracticesResponse response = chatbotService.generateMissions(chatThread.getSummary());

        // 트랜잭션 2: 미션 저장
        List<Mission> missions = transactionTemplate.execute(status -> {
            List<Mission> savedMissions = response.selfPractices().stream()
                    .map(practice -> Mission.builder()
                            .userChatThread(chatThread)
                            .tag(parseTag(practice.tag()))
                            .title(practice.title())
                            .content(practice.content())
                            .tip(practice.tip())
                            .dueDate(calculateDueDate(practice.duration()))
                            .reward(practice.reward())
                            .missionStatus(MissionStatus.ASSIGNED)
                            .build())
                    .toList();
            return missionRepository.saveAll(savedMissions);
        });

        return CreateMissionResponse.of(userChatThreadId, missions);
    }

    private Tag parseTag(String tag) {
        String normalized = tag.replace("-", "_").toUpperCase();
        return Tag.valueOf(normalized);
    }

    private LocalDateTime calculateDueDate(String duration) {
        LocalDateTime now = LocalDateTime.now();
        return switch (duration) {
            case "1일" -> now.plusDays(1);
            case "3일" -> now.plusDays(3);
            case "1주" -> now.plusWeeks(1);
            case "2주" -> now.plusWeeks(2);
            default -> now.plusWeeks(1);
        };
    }

    // 과제 수행 완료(XP 지급)
    @Transactional
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
