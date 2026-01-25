package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.request.RegisterCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.RegisterCharacterResponse;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.enums.CharacterKeyword;
import com.umc9th.areumdap.domain.character.enums.KeywordType;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.character.resolver.CharacterImageResolver;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterCommandService {

    private final CharacterImageResolver characterImageResolver;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final CharacterHistoryRepository characterHistoryRepository;

    // 캐릭터 성장
    public CharacterGrowthResponse levelUp(Long userId) {
        User user = getUser(userId);
        Character character = characterRepository.findByUserWithLock(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        int previousLevel = character.getLevel();
        character.tryLevelUp();

        characterHistoryRepository.save(new CharacterHistory(character, character.getLevel()));
        return CharacterGrowthResponse.from(character, previousLevel);
    }

    // 캐릭터 생성
    public RegisterCharacterResponse registerCharacter(Long userId, RegisterCharacterRequest request) {
        User user = getUser(userId);
        if (characterRepository.existsByUser(user))
            throw new GeneralException(ErrorStatus.CHARACTER_ALREADY_EXISTS);

        List<String> keywords = normalizeKeywords(request);
        Character character = characterRepository.save(
                Character.create(user, request.season(), keywords)
        );

        String imageUrl = characterImageResolver.resolve(character.getSeason(), character.getLevel());
        return new RegisterCharacterResponse(character.getId(),imageUrl);
    }

    // 캐릭터 XP 추가 (성장 가능 시 XP 추가 불가)
    public void addXpIfPossible(User user, int amount) {
        Character character = characterRepository.findByUserWithLock(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));

        if (character.getCurrentXp() >= character.getGoalXp()) {
            throw new GeneralException(ErrorStatus.CHARACTER_LEVEL_UP_REQUIRED);
        }

        character.addXp(amount);
    }


    // 캐릭터 키워드 가져오기
    private List<String> normalizeKeywords(RegisterCharacterRequest request) {

        if (request.keywordType() == KeywordType.CUSTOM) {
            return request.keywords();
        }

        return request.keywords().stream()
                .map(this::validatePresetKeyword)
                .toList();
    }

    // 키워드 값이 미리 정해진 형식일 경우 값이 맞는지를 검사
    private String validatePresetKeyword(String koreanKeyword) {

        return CharacterKeyword.fromKorean(koreanKeyword)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_CHARACTER_KEYWORD))
                .name();
    }

    // 유저 정보 가져오기
    private User getUser(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

}
