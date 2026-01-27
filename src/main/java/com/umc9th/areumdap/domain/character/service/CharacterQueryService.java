package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryDto;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMissionDto;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.enums.CharacterSeason;
import com.umc9th.areumdap.domain.character.resolver.CharacterImageResolver;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.character.enums.CharacterLevel;
import com.umc9th.areumdap.domain.mission.repository.MissionQueryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterQueryService {

    private final CharacterImageResolver characterImageResolver;
    private final CharacterRepository characterRepository;
    private final MissionQueryRepository missionQueryRepository;
    private final CharacterHistoryRepository characterHistoryRepository;

    // 자신의 캐릭터 조회
    public GetCharacterResponse getCharacter(Long userId) {
        Character character = getCharacterByUserId(userId);
        List<Mission> missionList = missionQueryRepository.findAllByUserChatThread_User_Id(userId);

        List<CharacterMissionDto> missions = missionList.stream()
                .filter(mission -> !mission.getDueDate().isBefore(LocalDateTime.now()))
                .map(CharacterMissionDto::from)
                .toList();

        boolean canLevelUp = character.getLevel() < CharacterLevel.LEVEL_4.getLevel()
                && character.getCurrentXp() >= character.getGoalXp();
        String imageUrl = characterImageResolver.resolve(character.getCharacterSeason(), character.getLevel());

        return GetCharacterResponse.from(character,canLevelUp,missions,imageUrl);
    }

    // 캐릭터 히스토리 조회
    public GetCharacterHistoryResponse getCharacterHistory(Long userId) {
        Character character = getCharacterByUserId(userId);
        List<CharacterHistory> historyList = characterHistoryRepository.findAllByCharacterOrderByCreatedAt(character);

        CharacterSeason season = character.getCharacterSeason();
        List<CharacterHistoryDto> responseList = historyList.stream()
                .map(history -> CharacterHistoryDto.of(history, characterImageResolver.resolve(season, history.getLevel())))
                .toList();

        return GetCharacterHistoryResponse.from(character, responseList);
    }

    // 유저 아이디로 캐릭터 조회
    public Character getCharacterByUserId(Long userId) {
        return characterRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));
    }

}
