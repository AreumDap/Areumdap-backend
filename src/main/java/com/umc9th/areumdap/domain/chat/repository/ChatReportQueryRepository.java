package com.umc9th.areumdap.domain.chat.repository;

import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatReportQueryRepository extends CrudRepository<ChatReport, Long> {
    Optional<ChatReport> findByIdAndUserChatThread_User_Id(Long reportId, Long userId);
    Optional<ChatReport> findByUserChatThread_Id(Long threadId);

}
