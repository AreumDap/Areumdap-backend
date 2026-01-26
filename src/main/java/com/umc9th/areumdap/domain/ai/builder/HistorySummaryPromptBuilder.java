package com.umc9th.areumdap.domain.ai.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.umc9th.areumdap.domain.chat.entity.UserChatThread;

import java.util.ArrayList;
import java.util.List;

public class HistorySummaryPromptBuilder {
    private static final ObjectMapper om =
            JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

    private static final String SYSTEM_PROMPT = """
            너는 ‘아름답’ 서비스의 회고 서술 AI이다.
            
            아름답은 정체성 혼란을 겪는 대학생이
            스스로 자신을 이해하도록 돕는 자기 성찰 서비스이며,
            AI는 조언이나 해결책을 제시하지 않고
            사용자가 자신의 과거 상태를 정리하도록 돕는 역할만 수행한다.
            
            너의 임무는,
            사용자와 AI가 나눈 여러 대화 세션을 요약한 텍스트들을 바탕으로
            “과거의 나는 이랬어요” 형식의 회고 문단을 작성하는 것이다.
            
            다음 원칙을 반드시 지켜라:
            
            1. ‘과거’는 해당 대화가 이루어지던 당시만을 의미한다.
               현재, 변화, 성장, 미래에 대한 언급은 금지한다.
            
            2. 출력은 반드시 1인칭 + 과거 시점 + 높임말 서술로 작성한다.
               (예: “나는 그때 …했어요”, “나는 …한 상태였어요”)
            
            3. 문장은 짧고 단정하게 작성한다.
               한 문장에 하나의 상태나 감정만 담아라.
               복합 문장, 긴 설명, 여운을 남기는 미완 문장은 사용하지 마라.
            
            4. 출력은 정확히 3~5문장으로 제한한다.
               각 문장은 반드시 줄바꿈(`\\n`)으로 구분하여 출력하라.
               한 줄에 한 문장만 작성하라.
            
            5. 톤은 차분하고 담담한 회고 형식이어야 한다.
               문학적 표현, 감정 과장, 서사적 흐름은 배제한다.
            
            6. 조언, 위로, 평가, 질문, 독자에게 말을 거는 표현은 절대 포함하지 마라.
            
            7. 입력에 명시되지 않은 사실을 단정하지 마라.
               단, 여러 요약에서 반복적으로 드러나는 경향은
               최소한으로 종합하여 서술할 수 있다.
            
            이 출력은 사용자가
            자신의 과거 상태를 안전하게 돌아보기 위한 기록이다.
            
            """;

    private static final String USER_PROMPT = """
            다음은 나와 챗봇 AI가 나눈 여러 대화 세션을 요약한 내용이다.
            이 요약들을 바탕으로 회고 문단을 작성하라.
            
            [대화 세션 요약 목록]
            
            {{session_summary}}
            """;

    public static String build(List<UserChatThread> chatThreads) {
        StringBuilder summaryBlock = new StringBuilder();

        chatThreads.forEach(c -> {
            summaryBlock
                    .append("- 요약 ")
                    .append(": ")
                    .append(c.getSummary())
                    .append("\n");
            });

        return SYSTEM_PROMPT + USER_PROMPT.replace(
                "{{session_summary}}",
                summaryBlock
        );
    }
}
