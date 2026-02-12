package com.umc9th.areumdap.domain.character.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.character.dto.request.RegisterCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.RegisterCharacterResponse;
import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import com.umc9th.areumdap.domain.character.enums.CharacterKeyword;
import com.umc9th.areumdap.domain.character.enums.CharacterSeason;
import com.umc9th.areumdap.domain.character.enums.KeywordType;
import com.umc9th.areumdap.domain.character.repository.CharacterHistoryRepository;
import com.umc9th.areumdap.domain.character.repository.CharacterRepository;
import com.umc9th.areumdap.domain.character.resolver.CharacterImageResolver;
import com.umc9th.areumdap.domain.chatbot.dto.response.HistorySummaryResponse;
import com.umc9th.areumdap.domain.chatbot.service.ChatbotService;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final ChatbotService chatbotService;

    // 캐릭터 성장
    public GetCharacterGrowthResponse levelUp(Long userId) {
        User user = getUser(userId);
        Character character = getCharacterByUser(user);

        int previousLevel = character.getLevel();
        character.tryLevelUp();
        int requiredXpForNextLevel = Math.max(0, character.getGoalXp() - character.getCurrentXp());

        characterHistoryRepository.save(new CharacterHistory(character, character.getLevel()));
        return GetCharacterGrowthResponse.of(character.getId(), previousLevel, character.getLevel(), character.getCurrentXp(), requiredXpForNextLevel);
    }

    // 캐릭터 생성
    public RegisterCharacterResponse registerCharacter(Long userId, RegisterCharacterRequest request) {
        User user = getUser(userId);
        List<String> keywords = normalizeKeywords(request);

        try {
            Character character = characterRepository.save(Character.create(user, request.characterSeason(), keywords));
            String imageUrl = characterImageResolver.resolve(character.getCharacterSeason(), character.getLevel());
            characterHistoryRepository.save(new CharacterHistory(character, character.getLevel()));
            return new RegisterCharacterResponse(character.getId(), imageUrl);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(ErrorStatus.CHARACTER_ALREADY_EXISTS);
        }
    }

    // 캐릭터 성장 히스토리 업데이트
    public void updateHistorySummary(Long userId) {
        User user = getUser(userId);
        Character character = getCharacterByUser(user);

        HistorySummaryResponse response = chatbotService.generateHistorySummary(userId);
        character.updateHistorySummary(
                response.pastDescription(),
                response.currentDescription()
        );
    }

    // 캐릭터 XP 추가 (성장 가능 시 XP 추가 불가)
    public void addXpIfPossible(User user, int amount) {
        Character character = getCharacterByUser(user);

        if (character.getCurrentXp() >= character.getGoalXp()) {
            throw new GeneralException(ErrorStatus.CHARACTER_LEVEL_UP_REQUIRED);
        }

        character.addXp(amount);
    }

    // 캐릭터 키워드 가져오기
    private List<String> normalizeKeywords(RegisterCharacterRequest request) {

        if (request.keywordType() == KeywordType.CUSTOM) {
            return request.keywords(); // 그대로 저장
        }

        // PRESET인 경우만 검증
        return request.keywords().stream()
                .map(keyword -> validatePresetKeyword(keyword, request.characterSeason()))
                .toList();
    }

    // 키워드 값이 미리 정해진 형식일 경우 값이 맞는지를 검사
    private String validatePresetKeyword(String koreanKeyword, CharacterSeason season) {
        CharacterKeyword keyword = CharacterKeyword.fromKorean(koreanKeyword)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_CHARACTER_KEYWORD));

        if (!season.supports(keyword)) {
            throw new GeneralException(ErrorStatus.INVALID_CHARACTER_KEYWORD);
        }

        return koreanKeyword; // 그대로 String 반환
    }

    // 유저 정보 가져오기
    private User getUser(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 캐릭터 정보 가져오기
    private Character getCharacterByUser(User user) {
        return characterRepository.findByUserWithLock(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHARACTER_NOT_FOUND));
    }

}
