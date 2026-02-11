package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatReportQueryRepository extends JpaRepository<ChatReport, Long> {
    Optional<ChatReport> findByIdAndUserChatThread_User_Id(Long reportId, Long userId);
    Optional<ChatReport> findByUserChatThread_Id(Long threadId);

}
