package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOnboardingQueryRepository extends JpaRepository<UserOnboarding, Long> {
}
