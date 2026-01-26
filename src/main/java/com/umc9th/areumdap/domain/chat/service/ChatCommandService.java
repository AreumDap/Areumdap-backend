package com.umc9th.areumdap.domain.chat.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserQuestionRepository;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatCommandService {

    private final UserChatThreadRepository userChatThreadRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;

    public CreateChatThreadResponse createChatThread(Long userId, CreateChatThreadRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        UserQuestion userQuestion = userQuestionRepository.findById(request.userQuestionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_QUESTION_NOT_FOUND));

        userQuestion.markAsUsed();

        UserChatThread userChatThread = UserChatThread.builder()
                .user(user)
                .questionBank(userQuestion.getQuestionBank())
                .favorite(false)
                .build();
        userChatThreadRepository.save(userChatThread);

        ChatHistory botHistory = ChatHistory.builder()
                .content(request.content())
                .userChatThread(userChatThread)
                .senderType(SenderType.BOT)
                .build();
        chatHistoryRepository.save(botHistory);

        return new CreateChatThreadResponse(request.content(), userChatThread.getId());
    }

}
