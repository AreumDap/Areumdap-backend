package com.umc9th.areumdap.domain.user.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.chat.entity.ChatHistory;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;
import com.umc9th.areumdap.domain.mission.enums.Tag;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "user_question", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "chat_history_id"})
})
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id", nullable = false)
    private QuestionBank questionBank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_user_question_id")
    private UserQuestion parentQuestion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_history_id")
    private ChatHistory chatHistory;

    @OneToMany(mappedBy = "userQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserChatThread> userChatThreads = new ArrayList<>();

    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private Boolean used = false;

    @Column(name = "is_saved", nullable = false)
    @Builder.Default
    private Boolean saved = false;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 태그 가져오기
    public Tag getTag() {
        return questionBank.getTag();
    }

    public void markAsUsed() {
        this.used = true;
    }

    public void markAsUnused() {
        this.used = false;
    }

    public void clearChatHistory() {
        this.chatHistory = null;
    }

    public void markAsUnsaved() {
        this.saved = false;
    }

    public void clearParentQuestion() {
        this.parentQuestion = null;
    }

}
