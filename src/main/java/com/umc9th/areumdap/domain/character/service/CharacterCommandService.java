package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.request.CharacterCreateRequest;
import com.umc9th.areumdap.domain.character.dto.response.CharacterCreateResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
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
    private final com.umc9th.areumdap.domain.user.repository.UserOnboardingRepository userOnboardingRepository;

    // 캐릭터 성장
    public CharacterGrowthResponse levelUp(Long userId) {
        User user = userQueryService.getUserByIdAndDeletedFalse(userId);

        Character character = characterRepository.findByUserWithLock(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        int previousLevel = character.getLevel();

        character.tryLevelUp();

        characterHistoryRepository.save(new CharacterHistory(character, character.getLevel()));

        return CharacterGrowthResponse.from(character, previousLevel);
    }

    // 캐릭터 생성
    public CharacterCreateResponse createCharacter(Long userId, CharacterCreateRequest request) {
        User user = userQueryService.getUserByIdAndDeletedFalse(userId);

        Character character = characterRepository.findByUser(user)
                .orElseGet(() -> characterRepository.save(new Character(user)));

        return new CharacterCreateResponse(
                character.getId(),
                userId,
                request.season()
        );
    }
}
