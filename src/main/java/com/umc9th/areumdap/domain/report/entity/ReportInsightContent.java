package com.umc9th.areumdap.domain.report.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "report_insight_content")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportInsightContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_report_id", nullable = false)
    private ChatReport chatReport;

    @Column(name = "insight_content", nullable = false, columnDefinition = "TEXT")
    private String insightContent;
}