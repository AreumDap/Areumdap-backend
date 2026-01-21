package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterCommandService {

    private final UserQueryService userQueryService;
    private final CharacterRepository characterRepository;
    private final CharacterHistoryRepository characterHistoryRepository;

    // 캐릭터 성장
    public CharacterGrowthResponse levelUp(Long userId) {
        User user = userQueryService.getUserByIdAndDeletedFalse(userId);

        Character character = characterRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));


        if (character.getLevel() >= CharacterLevel.LEVEL_4.getLevel()) {
            throw new GeneralException(ErrorStatus.CHARACTER_ALREADY_MAX_LEVEL);
        }

        if (character.getCurrentXp() < character.getGoalXp()) {
            throw new GeneralException(ErrorStatus.CHARACTER_GROWTH_NOT_ENOUGH_XP);
        }

        int previousLevel = character.getLevel();
        int nextGoalXp = CharacterLevel.getGoalXpByLevel(character.getLevel() + 1);
        
        character.levelUp(nextGoalXp);

        // 히스토리 저장
        characterHistoryRepository.save(CharacterHistory.builder()
                .character(character)
                .level(character.getLevel())
                .build());
        
        String characterName = "아름이";
        String growthMessage = String.format("%s가 %d단계로 성장했어요!", characterName, character.getLevel());
        int remainingXp = character.getGoalXp() - character.getCurrentXp();
        String nextLevelGuide = String.format("다음 성장을 위해선 %d XP가 필요해요.", remainingXp);

        if (character.getLevel() >= CharacterLevel.LEVEL_4.getLevel()) {
             nextLevelGuide = "최고 레벨입니다.";
        }

        return CharacterGrowthResponse.builder()
                .characterId(character.getId())
                .previousLevel(previousLevel)
                .currentLevel(character.getLevel())
                .growthMessage(growthMessage)
                .nextLevelGuide(nextLevelGuide)
                .build();
    }
}
