package com.umc9th.areumdap.domain.question.repository;

import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> { //문제은행에서 랜덤으로 pageable변수만큼 미션을 뽑는 쿼리
    @Query("SELECT qb FROM QuestionBank qb " +
           "WHERE qb.id NOT IN (SELECT uq.questionBank.id FROM UserQuestion uq WHERE uq.user.id = :userId AND uq.parentQuestion IS NULL) " +
           "AND qb.id != 91 " +
           "ORDER BY function('random')")
    List<QuestionBank> findRandomQuestions(
            @Param("userId") Long userId,
            Pageable pageable
    );

}
