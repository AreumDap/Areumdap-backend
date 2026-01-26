package com.umc9th.areumdap.domain.chat.service;

import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatThreadQueryService {
    private final UserChatThreadRepository userChatThreadRepository;

    public List<UserChatThread> findByUserId(Long userId){
        return this.userChatThreadRepository.findByUserId(userId);
    }
}
