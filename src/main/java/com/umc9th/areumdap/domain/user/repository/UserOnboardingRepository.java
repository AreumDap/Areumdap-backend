package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserOnboardingRepository extends JpaRepository<UserOnboarding, Long> {

    boolean existsByUser(User user);

    java.util.Optional<UserOnboarding> findByUser(User user);

    @org.springframework.data.jpa.repository.Query("SELECT uo FROM UserOnboarding uo JOIN FETCH uo.user u WHERE u.id = :userId AND u.deleted = false")
    java.util.Optional<UserOnboarding> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
