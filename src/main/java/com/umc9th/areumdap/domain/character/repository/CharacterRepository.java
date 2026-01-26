package com.umc9th.areumdap.domain.character.repository;

import com.umc9th.areumdap.domain.character.entity.Character;
import com.umc9th.areumdap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("SELECT c FROM Character c JOIN FETCH c.user u WHERE u.id = :userId AND u.deleted = false")
    Optional<Character> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Character c JOIN c.user u WHERE u = :user AND u.deleted = false")
    Optional<Character> findByUserWithLock(@Param("user") User user);

    boolean existsByUser(User user);
}
