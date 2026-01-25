package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryDto;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMeResponse;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import com.umc9th.areumdap.domain.user.repository.UserOnboardingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterQueryService {


    private final CharacterRepository characterRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final CharacterHistoryRepository characterHistoryRepository;

    // 캐릭터 조회
    public CharacterMeResponse getCharacterMain(Long userId) {
        
        Character character = characterRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        UserOnboarding userOnboarding = userOnboardingRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_ONBOARDING_NOT_FOUND));



        boolean canLevelUp = character.getLevel() < CharacterLevel.LEVEL_4.getLevel()
                && character.getCurrentXp() >= character.getGoalXp();

        return CharacterMeResponse.builder()
                .characterId(character.getId())
                .nickname(userOnboarding.getNickname())
                .level(character.getLevel())
                .currentXp(character.getCurrentXp())
                .goalXp(character.getGoalXp())
                .hasLevelUpParams(canLevelUp)
                .build();
    }

    // 캐릭터 히스토리 조회
    public CharacterHistoryResponse getCharacterHistory(Long userId) {
        Character character = characterRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        List<CharacterHistory> historyList = characterHistoryRepository.findAllByCharacterOrderByCreatedAt(character);

        List<CharacterHistoryDto> responseList = new java.util.ArrayList<>();
        responseList.add(new CharacterHistoryDto(1, character.getCreatedAt().toLocalDate()));
        
        responseList.addAll(historyList.stream()
                .map(history -> new CharacterHistoryDto(history.getLevel(), history.getCreatedAt().toLocalDate()))
                .toList());

        return CharacterHistoryResponse.builder()
                .pastDescription(character.getPastDescription())
                .presentDescription(character.getPresentDescription())
                .historyList(responseList)
                .build();
    }
}
