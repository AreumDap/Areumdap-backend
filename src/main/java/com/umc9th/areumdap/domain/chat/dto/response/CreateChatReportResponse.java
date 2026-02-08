package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.domain.report.dto.response.ReportInsightResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportTagResponse;

import java.time.LocalDate;
import java.util.List;

public record CreateChatReportResponse(
        Long reportId,
        String title,
        Integer messageCount,
        String depth,
        Integer durationMinutes,
        String summaryContent,
        List<ReportTagResponse> reportTags,
        List<ReportInsightResponse> insightContents,
        LocalDate createdAt
) {
}
