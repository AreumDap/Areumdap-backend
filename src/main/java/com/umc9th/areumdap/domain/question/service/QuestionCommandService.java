package com.umc9th.areumdap.domain.question.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import com.umc9th.areumdap.domain.question.repository.QuestionBankRepository;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.repository.UserQuestionRepository;
import com.umc9th.areumdap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionCommandService {

    private final QuestionBankRepository questionBankRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final UserRepository userRepository;

    public List<UserQuestion> assignRandomQuestions(Long userId){
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<QuestionBank> randomQuestions = questionBankRepository.findRandomQuestions(userId, PageRequest.of(0, 5));

        if (randomQuestions.size() < 5) {
            throw new GeneralException(ErrorStatus.QUESTION_BANK_NOT_ENOUGH);
        }

        List<UserQuestion> userQuestions = randomQuestions.stream()
                .map(q -> UserQuestion.builder()
                        .user(user)
                        .questionBank(q)
                        .content(q.getContent())
                        .tag(q.getTag())
                        .used(false)
                        .build())
                .toList();
        return userQuestionRepository.saveAll(userQuestions);
    }
}
