package com.umc9th.areumdap.domain.chat.entity;

import com.umc9th.areumdap.common.base.BaseEntity;
import com.umc9th.areumdap.domain.mission.entity.Mission;
import com.umc9th.areumdap.domain.question.entity.Question;
import com.umc9th.areumdap.domain.report.entity.ChatReport;
import com.umc9th.areumdap.domain.user.entity.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChatHistory> chatHistories = new ArrayList<>();

    @OneToOne(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    private ChatReport chatReport;

    @OneToMany(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "userChatThread", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}
