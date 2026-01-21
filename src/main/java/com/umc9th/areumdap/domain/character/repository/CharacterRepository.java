package com.umc9th.areumdap.domain.character.repository;

import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByUser(User user);

    @org.springframework.data.jpa.repository.Query("SELECT c FROM Character c JOIN FETCH c.user u WHERE u.id = :userId AND u.deleted = false")
    Optional<Character> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
