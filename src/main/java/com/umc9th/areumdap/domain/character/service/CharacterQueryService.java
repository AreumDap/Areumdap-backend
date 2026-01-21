package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryDto;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMainResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterQuestDto;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.entity.Quest;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.character.repository.QuestRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import com.umc9th.areumdap.domain.user.repository.UserOnboardingRepository;

import com.umc9th.areumdap.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterQueryService {


    private final CharacterRepository characterRepository;
    private final QuestRepository questRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final CharacterHistoryRepository characterHistoryRepository;

    // 캐릭터 조회
    public CharacterMainResponse getCharacterMain(Long userId) {
        
        Character character = characterRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        UserOnboarding userOnboarding = userOnboardingRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_ONBOARDING_NOT_FOUND));
        
        List<Quest> questList = questRepository.findAllByCharacter(character);

        List<CharacterQuestDto> quests = questList.stream()
                .map(quest -> CharacterQuestDto.of(
                        quest.getId(),
                        quest.getCategory(),
                        quest.getTitle(),
                        quest.getRemainingDays(),
                        quest.getIsCompleted()
                ))
                .toList();

        return CharacterMainResponse.builder()
                .characterId(character.getId())
                .nickname(userOnboarding.getNickname())
                .level(character.getLevel())
                .currentXp(character.getCurrentXp())
                .goalXp(character.getGoalXp())
                .hasLevelUpParams(false)
                .quests(quests)
                .build();
    }

    // 캐릭터 히스토리 조회
    public CharacterHistoryResponse getCharacterHistory(Long userId) {
        Character character = characterRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        List<CharacterHistory> historyList = characterHistoryRepository.findAllByCharacter(character);
        
        List<CharacterHistoryDto> responseList = new java.util.ArrayList<>();
        responseList.add(CharacterHistoryDto.of(1, character.getCreatedAt().toLocalDate()));
        
        responseList.addAll(historyList.stream()
                .map(history -> CharacterHistoryDto.of(history.getLevel(), history.getCreatedAt().toLocalDate()))
                .toList());

        return CharacterHistoryResponse.builder()
                .pastDescription(character.getPastDescription())
                .presentDescription(character.getPresentDescription())
                .historyList(responseList)
                .build();
    }
}
