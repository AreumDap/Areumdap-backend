package com.umc9th.areumdap.domain.character.repository;

import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.CharacterHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterHistoryRepository extends JpaRepository<CharacterHistory, Long> {
    List<CharacterHistory> findAllByCharacterOrderByCreatedAt(Character character);
}
