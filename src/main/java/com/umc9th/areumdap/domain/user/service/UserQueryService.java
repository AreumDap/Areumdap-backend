package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.dto.response.GetUserProfileResponse;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserQueryRepository userQueryRepository;

    // 유저 프로필 조회
    public GetUserProfileResponse getUserProfile(Long userId) {
        User user = getUserByIdAndDeletedFalse(userId);
        return GetUserProfileResponse.from(user);
    }

    // 이메일 중복 검사
    public void checkEmailNotExists(String email) {
        if (userQueryRepository.findByEmail(email).isPresent()) {
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
    }

    // 로그인 시 회원 탈퇴 여부 검사 및 User 반환
    public User getUserByEmailAndDeletedFalse(String email) {
        return userQueryRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_ALREADY_DELETED));
    }

    // 유저 아이디 + 삭제 여부로 유저 정보 가져오기
    public User getUserByIdAndDeletedFalse(Long userId) {
        return userQueryRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

}
