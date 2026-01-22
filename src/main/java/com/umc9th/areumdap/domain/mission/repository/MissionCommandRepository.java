package com.umc9th.areumdap.domain.mission.repository;

import com.umc9th.areumdap.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionCommandRepository extends JpaRepository<Mission,Long> {
}
