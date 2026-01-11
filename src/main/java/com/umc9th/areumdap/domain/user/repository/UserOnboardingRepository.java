package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserOnboardingRepository extends JpaRepository<UserOnboarding, Long> {

    boolean existsByUser(User user);
}
