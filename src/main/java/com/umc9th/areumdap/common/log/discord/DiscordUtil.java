package com.umc9th.areumdap.common.log.discord;

import com.umc9th.areumdap.common.log.discord.dto.EmbedDto;
import com.umc9th.areumdap.common.log.discord.dto.MessageDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Component
public class DiscordUtil {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");

    public MessageDto createMessage(
            Exception exception,
            HttpServletRequest request
    ) {
        return new MessageDto(
                "# 🚨 서버 에러 발생 🚨",
                List.of(createErrorEmbed(exception, request))
        );
    }

    private EmbedDto createErrorEmbed(Exception exception, HttpServletRequest request) {
        String description =
                "### 에러 발생 시간\n" +
                        now() +
                        "\n### 요청 엔드포인트\n" +
                        endpoint(request) +
                        "\n### 요청 클라이언트\n" +
                        client(request) +
                        "\n### 에러 스택 트레이스\n" +
                        "```\n" +
                        stackTrace(exception) +
                        "\n```";

        return new EmbedDto("에러 정보", description);
    }

    /**
     * 한국 시간(KST) 기준 현재 시각을 포맷된 문자열로 반환
     */
    private String now() {
        return ZonedDateTime.now(KST).format(TIME_FORMAT);
    }

    /**
     * 요청 HTTP 메서드와 URL을 문자열로 변환
     */
    private String endpoint(HttpServletRequest request) {
        return "[" + request.getMethod() + "] " + request.getRequestURL();
    }

    /**
     * 요청 클라이언트 IP와 사용자 정보(UserId)를 문자열로 구성
     */
    private String client(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String user = request.getUserPrincipal() != null
                ? " / [UserId]: " + request.getUserPrincipal().getName()
                : "";
        return "[IP]: " + ip + user;
    }

    /**
     * 예외 스택 트레이스를 최대 1000자까지 문자열로 추출
     */
    private String stackTrace(Exception e) {
        String trace = e.toString() + "\n" +
                String.join(
                        "\n",
                        Stream.of(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                );

        return trace.substring(0, Math.min(1000, trace.length()));
    }

}
