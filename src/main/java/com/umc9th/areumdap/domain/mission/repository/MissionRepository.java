package com.umc9th.areumdap.domain.mission.repository;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Mission m JOIN FETCH m.userChatThread uct JOIN FETCH uct.user WHERE m.id = :id")
    Optional<Mission> findByIdWithUserForUpdate(@Param("id") Long id);
}
