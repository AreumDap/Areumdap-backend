package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueryRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
