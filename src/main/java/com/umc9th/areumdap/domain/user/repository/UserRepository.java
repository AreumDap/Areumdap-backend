package com.umc9th.areumdap.domain.user.repository;

import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByIdAndDeletedFalse(Long id);
    Optional<User> findByOauthIdAndOauthProviderAndDeletedFalse(String oauthId, OAuthProvider oauthProvider);
}
