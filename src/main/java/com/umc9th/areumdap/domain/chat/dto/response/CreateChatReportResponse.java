package com.umc9th.areumdap.domain.chat.dto.response;

import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import com.umc9th.areumdap.domain.report.dto.response.ReportInsightResponse;
import com.umc9th.areumdap.domain.report.dto.response.ReportTagResponse;
import com.umc9th.areumdap.domain.report.entity.ReportInsightContent;
import com.umc9th.areumdap.domain.report.entity.ReportTag;

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
    public static CreateChatReportResponse from(ChatReport chatReport,
                                                List<ReportTag> reportTags,
                                                List<ReportInsightContent> insights) {
        List<ReportTagResponse> tagResponses = reportTags.stream()
                .map(tag -> new ReportTagResponse(tag.getReportTag()))
                .toList();

        List<ReportInsightResponse> insightResponses = insights.stream()
                .map(insight -> new ReportInsightResponse(insight.getId(), insight.getInsightContent()))
                .toList();

        return new CreateChatReportResponse(
                chatReport.getId(),
                chatReport.getTitle(),
                chatReport.getMessageCount(),
                chatReport.getDepth(),
                chatReport.getDurationMinutes(),
                chatReport.getSummaryContent(),
                tagResponses,
                insightResponses,
                chatReport.getCreatedAt().toLocalDate()
        );
    }
}
