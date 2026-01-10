package com.umc9th.areumdap.domain.user.service;


import com.umc9th.areumdap.domain.user.repository.UserOnboardingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserOnboardingQueryService {

    private final UserOnboardingQueryRepository userOnboardingQueryRepository;
}
