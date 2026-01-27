package com.umc9th.areumdap.domain.chat.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.question.entity.QuestionBank;
import com.umc9th.areumdap.domain.user.entity.User;
import com.umc9th.areumdap.domain.user.entity.UserQuestion;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "user_chat_thread")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserChatThread extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id")
    private QuestionBank questionBank;

    @Column(name = "summary")
    private String summary;

    @Column(name = "favorite", nullable = false)
    @Builder.Default
    private boolean favorite = false;

    @OneToMany(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChatHistory> chatHistories = new ArrayList<>();

    @OneToOne(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    private ChatReport chatReport;

    @OneToMany(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Mission> missions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_question_id", nullable = true)
    private UserQuestion userQuestion;

}
