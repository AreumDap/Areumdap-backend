package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMainResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterQuestDto;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.Quest;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.character.repository.QuestRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final QuestRepository questRepository;

    public CharacterMainResponse getCharacterMain(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        
        Character character = characterRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        List<Quest> questList = questRepository.findAllByCharacter(character);

        List<CharacterQuestDto> quests = questList.stream()
                .map(quest -> CharacterQuestDto.of(
                        quest.getId(),
                        quest.getCategory(),
                        quest.getTitle(),
                        (int) ChronoUnit.DAYS.between(LocalDate.now(), quest.getDeadline()),
                        quest.isCompleted()
                ))
                .toList();

        return CharacterMainResponse.builder()
                .characterId(character.getId())
                .name(user.getName())
                .level(character.getLevel())
                .currentXp(character.getCurrentXp())
                .goalXp(character.getGoalXp())
                .hasLevelUpParams(false)
                .quests(quests)
                .build();
    }
}
