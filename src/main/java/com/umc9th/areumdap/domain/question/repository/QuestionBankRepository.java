package com.umc9th.areumdap.domain.question.repository;

import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> { //문제은행에서 랜덤으로 count만큼 미션을 뽑는 쿼리
    @Query(value = "SELECT * FROM question_bank WHERE id NOT IN(" +
            "SELECT question_bank_id FROM user_question WHERE user_id = :userId" +
            ") ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<QuestionBank> findRandomQuestions(@Param("userId") Long userId,
                                                     @Param("count") int count
    );
}
