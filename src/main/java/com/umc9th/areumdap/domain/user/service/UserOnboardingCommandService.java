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
    public Long registerUserOnboarding(
            User user,
            Season season,
            List<String> keywords,
            Long characterId,
            String nickname
    ){
        UserOnboarding userOnboarding = userOnboardingRepository.findByUser(user)
                .orElseGet(() -> UserOnboarding.builder()
                        .user(user)
                        .season(season)
                        .build());

        if (userOnboarding.getNickname() != null) {
            throw new GeneralException(ErrorStatus.USER_ONBOARDING_ALREADY_EXISTS);
        }

        userOnboarding.completeOnboarding(season, keywords, characterId, nickname);

        return userOnboardingRepository.save(userOnboarding).getId();    }
}
