package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    // 이메일 중복 검사
    public void checkEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
    }

    // 이메일로 유저 정보 가져오기
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EMAIL_NOT_FOUND2));
    }
}
