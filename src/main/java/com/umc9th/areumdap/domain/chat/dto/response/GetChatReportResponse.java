package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.domain.mission.dto.response.MissionSummaryResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportInsightResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportTagResponse;

import java.time.LocalDate;
import java.util.List;

public record GetChatReportResponse(
        String title,
        Integer messageCount,
        String depth,
        Integer durationMinutes,
        String summaryContent,
        List<ReportTagResponse> reportTags ,
        List<ReportInsightResponse> insightContents,
        List<MissionSummaryResponse> missions,
        LocalDate createdAt

) {
}
