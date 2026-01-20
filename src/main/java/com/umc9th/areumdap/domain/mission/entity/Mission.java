package com.umc9th.areumdap.domain.mission.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.mission.enums.MissionStatus;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Table(name = "mission")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private UserChatThread userChatThread;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", columnDefinition = "tag_enum",nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Tag tag;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "tip", nullable = false, columnDefinition = "TEXT")
    private String tip;

    @Column(name = "duedate",nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "reward",nullable = false)
    private Long reward;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_status", columnDefinition = "mission_status_enum",nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private MissionStatus missionStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
