package com.umc9th.areumdap.domain.user.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserQuestionRepository;
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

        /*
         * 질문 계보 결정
         * - thread가 UserQuestion에서 시작된 경우 → parentQuestion 존재
         * - thread가 QuestionBank에서 시작된 경우 → parentQuestion 없음
         */
        UserQuestion parentQuestion = thread.getUserQuestion(); // nullable
        QuestionBank questionBank;

        if (parentQuestion != null) {
            // UserQuestion → UserQuestion 파생
            questionBank = parentQuestion.getQuestionBank();
        } else {
            // QuestionBank → UserQuestion
            questionBank = thread.getQuestionBank();
            if (questionBank == null) {
                throw new GeneralException(ErrorStatus.QUESTION_BANK_NOT_FOUND);
            }
        }

        UserQuestion userQuestion = UserQuestion.builder()
                .user(user)
                .questionBank(questionBank)              // 루트 Bank 유지
                .parentQuestion(parentQuestion)
                .chatHistory(chatHistory)
                .content(chatHistory.getContent())       // 즉석 질문 본문
                .used(false)
                .build();

        userQuestionRepository.save(userQuestion);
    }

}
