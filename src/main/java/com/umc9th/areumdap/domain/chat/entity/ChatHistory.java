package com.umc9th.areumdap.domain.chat.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.enums.SenderType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Table(name = "chatHistory")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private UserChatThread userChatThread;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", columnDefinition = "sender_type_enum",nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SenderType senderType;

}
