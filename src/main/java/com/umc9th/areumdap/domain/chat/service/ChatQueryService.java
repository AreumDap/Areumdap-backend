package com.umc9th.areumdap.domain.chat.service;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.chat.dto.response.GetChatReportResponse;
import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import com.umc9th.areumdap.domain.chat.repository.ChatReportRepository;
import com.umc9th.areumdap.domain.mission.dto.response.MissionSummaryResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportInsightResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportTagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatReportRepository chatReportRepository;

    public GetChatReportResponse getChatReport(Long userId, Long reportId) {

        ChatReport report = chatReportRepository
                .findByIdAndUserChatThread_User_Id(reportId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_REPORT_NOT_FOUND));

        List<MissionSummaryResponse> missions = report.getUserChatThread()
                .getMissions()
                .stream()
                .map(mission -> new MissionSummaryResponse(
                        mission.getId(),
                        mission.getTag(),
                        mission.getTitle(),
                        mission.getContent(),
                        mission.getMissionStatus()
                ))
                .toList();

        List<ReportTagResponse> tags = report.getReportTags()
                .stream()
                .map(tag -> new ReportTagResponse(tag.getReportTag()))
                .toList();

        List<ReportInsightResponse> insights = report.getInsights()
                .stream()
                .map(insight -> new ReportInsightResponse(
                        insight.getId(),
                        insight.getInsightContent()
                ))
                .toList();

        return new GetChatReportResponse(
                report.getTitle(),
                report.getMessageCount(),
                report.getDepth(),
                report.getDurationMinutes(),
                report.getSummaryContent(),
                tags,
                insights,
                missions,
                report.getCreatedAt().toLocalDate()
        );
    }
}
