package com.umc9th.areumdap.domain.chat.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.dto.request.ChatSummaryRequest;
import com.umc9th.areumdap.domain.chat.dto.request.CreateChatThreadRequest;
import com.umc9th.areumdap.domain.chat.dto.request.SendChatMessageRequest;
import com.umc9th.areumdap.domain.chat.dto.response.ChatSummaryResponse;
import com.umc9th.areumdap.domain.chat.dto.response.CreateChatThreadResponse;
import com.umc9th.areumdap.domain.chat.dto.response.SendChatMessageResponse;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import com.umc9th.areumdap.domain.chat.repository.ChatHistoryRepository;
import com.umc9th.areumdap.domain.chat.repository.UserChatThreadRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.user.repository.UserQuestionRepository;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import com.umc9th.areumdap.domain.chatbot.service.ChatbotService;
import com.umc9th.areumdap.domain.chatbot.service.ChatbotService.ChatbotResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCommandService {

    private final UserChatThreadRepository userChatThreadRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final ChatbotService chatbotAiService;
    private final ChatCacheService chatCacheService;
    private final TransactionTemplate transactionTemplate;

    private User getUser(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Transactional
    public CreateChatThreadResponse createChatThread(Long userId, CreateChatThreadRequest request) {
        User user = getUser(userId);

        UserQuestion userQuestion = userQuestionRepository.findById(request.userQuestionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_QUESTION_NOT_FOUND));

        userQuestion.markAsUsed();

        UserChatThread userChatThread = UserChatThread.builder()
                .user(user)
                .userQuestion(userQuestion)
                .questionBank(userQuestion.getQuestionBank())
                .userQuestion(userQuestion)
                .favorite(false)
                .build();
        userChatThreadRepository.save(userChatThread);

        ChatHistory botHistory = ChatHistory.builder()
                .content(request.content())
                .userChatThread(userChatThread)
                .senderType(SenderType.BOT)
                .build();
        chatHistoryRepository.save(botHistory);

        // Redis 캐시 초기화 (첫 BOT 메시지 저장)
        chatCacheService.setChatHistories(userChatThread.getId(), List.of(botHistory));

        return new CreateChatThreadResponse(request.content(), userChatThread.getId());
    }

    public SendChatMessageResponse sendChatMessage(Long userId, SendChatMessageRequest request) {
        // 트랜잭션 1: 유저 확인 + 채팅 스레드 조회 + 유저 메시지 저장
        UserChatThread chatThread = transactionTemplate.execute(status -> {
            User user = getUser(userId);

            UserChatThread thread = userChatThreadRepository.findById(request.userChatThreadId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND));

            if (!thread.getUser().getId().equals(userId)) {
                throw new GeneralException(ErrorStatus.CHAT_THREAD_ACCESS_DENIED);
            }

            // 유저 메시지 저장
            ChatHistory userMessage = ChatHistory.builder()
                    .content(request.content())
                    .userChatThread(thread)
                    .senderType(SenderType.USER)
                    .build();
            chatHistoryRepository.save(userMessage);

            return thread;
        });

        // AI 응답 생성 (트랜잭션 외부 - DB 커넥션 점유 X)
        ChatbotResponseResult result = chatbotAiService.generateResponse(chatThread, request.content());

        // 트랜잭션 2: AI 응답 저장
        transactionTemplate.executeWithoutResult(status -> {
            ChatHistory botMessage = ChatHistory.builder()
                    .content(result.content())
                    .userChatThread(chatThread)
                    .senderType(SenderType.BOT)
                    .build();
            chatHistoryRepository.save(botMessage);
        });

        // Redis 캐시 업데이트 (트랜잭션 불필요)
        chatCacheService.addMessage(chatThread.getId(), request.content(), SenderType.USER);
        chatCacheService.addMessage(chatThread.getId(), result.content(), SenderType.BOT);

        return new SendChatMessageResponse(result.content(), chatThread.getId(), result.sessionEnd());
    }

    public ChatSummaryResponse generateSummary(Long userId, ChatSummaryRequest request) {
        // 트랜잭션 1: 스레드 조회 + 권한 확인 + 대화 내역 조회
        UserChatThread chatThread = transactionTemplate.execute(status -> {
            User user = getUser(userId);

            UserChatThread thread = userChatThreadRepository.findById(request.userChatThreadId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND));

            if (!thread.getUser().getId().equals(userId)) {
                throw new GeneralException(ErrorStatus.CHAT_THREAD_ACCESS_DENIED);
            }

            return thread;
        });

        List<ChatHistory> histories = chatHistoryRepository
                .findByUserChatThreadOrderByCreatedAtAsc(chatThread);

        // 대화 메타데이터 계산
        LocalDateTime startedAt = histories.get(0).getCreatedAt();
        LocalDateTime endedAt = histories.get(histories.size() - 1).getCreatedAt();
        int durationMinutes = (int) java.time.Duration.between(startedAt, endedAt).toMinutes();
        int messageCount = histories.size();

        // AI 요약 생성 (트랜잭션 외부 - DB 커넥션 점유 X)
        String summary = chatbotAiService.summarizeConversation(chatThread);

        // 트랜잭션 2: 요약 저장
        transactionTemplate.executeWithoutResult(status -> {
            chatThread.updateSummary(summary);
        });

        return new ChatSummaryResponse(
                summary,
                chatThread.getId(),
                startedAt,
                endedAt,
                durationMinutes,
                messageCount
        );
    }

    @Transactional
    public void updateFavorite(Long userId, Long threadId) {
        UserChatThread chatThread = userChatThreadRepository.findById(threadId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND));

        if (!chatThread.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.CHAT_THREAD_ACCESS_DENIED);
        }

        chatThread.updateFavorite();
    }

    @Transactional
    public void deleteChatThread(Long userId, Long userChatThreadId) {
        UserChatThread chatThread = userChatThreadRepository.findById(userChatThreadId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_THREAD_NOT_FOUND));

        if (!chatThread.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.CHAT_THREAD_ACCESS_DENIED);
        }

        // ChatHistory를 참조하는 UserQuestion의 chatHistory를 null로 설정하고 used를 false로 변경
        List<UserQuestion> userQuestions = userQuestionRepository.findByChatHistory_UserChatThread_Id(userChatThreadId);
        for (UserQuestion uq : userQuestions) {
            uq.clearChatHistory();
            uq.markAsUnused();
        }

        // UserQuestion used false로 변경
        chatThread.getUserQuestion().markAsUnused();

        userChatThreadRepository.delete(chatThread);

        chatCacheService.invalidateCache(userChatThreadId);
    }
}
