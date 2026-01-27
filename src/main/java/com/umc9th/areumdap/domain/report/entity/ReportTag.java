package com.umc9th.areumdap.domain.report.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.entity.ChatReport;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "report_tag")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_report_id", nullable = false)
    private ChatReport chatReport;

    @Column(name = "report_tag",length = 10 ,nullable = false)
    private String reportTag;
}