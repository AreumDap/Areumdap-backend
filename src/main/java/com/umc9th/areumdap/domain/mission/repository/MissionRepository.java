package com.umc9th.areumdap.domain.mission.repository;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findAllByUserChatThread_User_Id(Long userId);

    @Query("SELECT m FROM Mission m JOIN FETCH m.userChatThread uct JOIN FETCH uct.user WHERE m.id = :id")
    Optional<Mission> findByIdWithUser(@Param("id") Long id);
}
