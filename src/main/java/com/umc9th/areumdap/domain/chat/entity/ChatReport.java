package com.umc9th.areumdap.domain.chat.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.report.entity.ReportInsightContent;
import com.umc9th.areumdap.domain.report.entity.ReportTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "chat_report")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_chat_thread_id", nullable = false)
    private UserChatThread userChatThread;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message_count", nullable = false)
    private Integer messageCount;

    @Column(name = "depth", nullable = false, length = 20)
    private String depth;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "summary_content", nullable = false, columnDefinition = "TEXT")
    private String summaryContent;

    @OneToMany(mappedBy = "chatReport", fetch = FetchType.LAZY)
    private List<ReportTag> reportTags;

    @OneToMany(mappedBy = "chatReport", fetch = FetchType.LAZY)
    private List<ReportInsightContent> insights;

}
