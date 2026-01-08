package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.enums.OAuthProvider;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    // 유저 등록
    public void registerUser(String name, LocalDate birth,String email, String password) {
        Long age = (long) (LocalDate.now().getYear()-birth.getYear()+1);
        userRepository.save(
                User.builder()
                        .oauthProvider(OAuthProvider.EMAIL)
                        .name(name)
                        .birth(birth)
                        .age(age)
                        .email(email)
                        .password(password)
                        .isDeleted(false)
                        .build()
        );
    }

}
