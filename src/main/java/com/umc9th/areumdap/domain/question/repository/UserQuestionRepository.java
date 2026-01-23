package com.umc9th.areumdap.domain.question.repository;

import com.umc9th.areumdap.domain.question.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
}
