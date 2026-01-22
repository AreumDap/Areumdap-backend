package com.umc9th.areumdap.domain.character.repository;

import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.character.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findAllByCharacter(Character character);
}
