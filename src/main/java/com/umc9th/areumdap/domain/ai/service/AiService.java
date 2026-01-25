package com.umc9th.areumdap.domain.ai.service;

import com.umc9th.areumdap.domain.ai.builder.HistorySummaryPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    private final ChatClient chatClient;

    public String test() {
        String[] summaries = {
                " 사람들과 대화할 때 무슨 말을 해야 할지 몰라서 말수가 줄어든다고 이야기함",
                " 감정을 말로 표현하려고 하면 괜히 과장하는 것처럼 느껴져서 아예 말하지 않게 된다고 함",
                " 새로운 사람을 만나는 상황을 자주 피하게 되고, 그로 인해 혼자 있는 시간이 늘어났다고 말함",
                " 대화를 망칠까 봐 말을 꺼내기 전에 계속 머릿속으로 시뮬레이션을 돌린다고 표현함",
                " 상대방의 반응에 지나치게 신경 쓰게 되어 대화 후에 혼자 계속 곱씹는다고 함",
                " 자신의 감정을 정확히 모르겠다고 느끼며, 좋음과 나쁨 사이에서 헷갈린다고 말함",
                " 말을 하지 않으면 편해지지만, 동시에 사람들과 멀어지는 느낌이 들어 복잡하다고 이야기함",
                " 감정을 글로 쓰는 것이 말로 표현하는 것보다는 조금 더 수월하다고 언급함",
                " 사람들 앞에서 솔직해지면 부담을 주는 사람이 될까 봐 걱정된다고 말함",
                " 혼자 있는 시간이 익숙해졌지만, 이 상태가 계속되는 것이 맞는지는 잘 모르겠다고 표현함",
        };
        String prompt = HistorySummaryPromptBuilder.build(summaries);

        String raw = chatClient.call(prompt);
        log.debug("1");
        log.debug(raw);
        return raw;
    }
}
