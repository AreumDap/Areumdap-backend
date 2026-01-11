package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserOnboarding;
import com.umc9th.areumdap.domain.user.enums.Season;
import com.umc9th.areumdap.domain.user.repository.UserOnboardingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOnboardingCommandService {

    private final UserOnboardingRepository userOnboardingRepository;

    // 유저 온보딩 저장
    public void registerUserOnboarding(
            User user,
            List<Season> seasons,
            List<String> keywords,
            Long characterId,
            String nickname
    ){
        if(userOnboardingRepository.existsByUser(user))
            throw new GeneralException(ErrorStatus.USER_ONBOARDING_ALREADY_EXISTS);

        userOnboardingRepository.save(
                UserOnboarding.builder()
                        .user(user)
                        .seasons(seasons)
                        .keywords(keywords)
                        .characterId(characterId)
                        .nickname(nickname)
                        .build()
        );
    }

}
