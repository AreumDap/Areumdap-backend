package com.umc9th.areumdap.domain.character.repository;

import com.umc9th.areumdap.domain.character.entity.Character;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CharacterQueryRepository extends CrudRepository<Character, Long> {
    @Query("SELECT c FROM Character c JOIN FETCH c.user u WHERE u.id = :userId AND u.deleted = false")
    Optional<Character> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

}
