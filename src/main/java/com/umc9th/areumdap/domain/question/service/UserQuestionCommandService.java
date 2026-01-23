package com.umc9th.areumdap.domain.question.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import com.umc9th.areumdap.domain.question.entity.UserQuestion;
import com.umc9th.areumdap.domain.question.repository.UserQuestionRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQuestionCommandService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;

    public void saveInstantQuestion(Long userId, Long chatHistoryId) {

        // ChatHistory 조회
        ChatHistory chatHistory = chatHistoryRepository.findById(chatHistoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_HISTORY_NOT_FOUND));

        // 해당 채팅 스레드 소유자 검증
        UserChatThread thread = chatHistory.getUserChatThread();
        if (thread == null || thread.getUser() == null || thread.getUser().getId() == null) {
            throw new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND);
        }
        if (!thread.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        // 중복 저장 방지 (같은 chatHistory로 1번만 저장)
        if (userQuestionRepository.existsByUser_IdAndChatHistory_Id(userId, chatHistoryId)) {
            throw new GeneralException(ErrorStatus.ALREADY_SAVED_QUESTION);
        }

        // 유저 엔티티 조회
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 대화 시작 질문(관리자 Bank 질문) 조회
        QuestionBank startBank = thread.getQuestionBank();
        if (startBank == null) {
            throw new GeneralException(ErrorStatus.QUESTION_BANK_NOT_FOUND);
        }

        // 태그 결정: 시작 Bank의 태그를 컨텍스트로 사용
        Tag contextTag = startBank.getTag();

        // 즉석 질문 저장
        UserQuestion userQuestion = UserQuestion.builder()
                .questionBank(startBank)                  // 대화 시작 질문
                .user(user)
                .chatHistory(chatHistory)
                .content(chatHistory.getContent())        // 즉석 질문 본문
                .tag(contextTag)                           // 컨텍스트 태그
                .build();

        userQuestionRepository.save(userQuestion);
    }

}
