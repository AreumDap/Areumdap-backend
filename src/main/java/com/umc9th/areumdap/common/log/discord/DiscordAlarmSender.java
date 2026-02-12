package com.umc9th.areumdap.common.log.discord;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@Profile({"prod"})
public class DiscordAlarmSender {

    @Value("${logging.discord.webhook-url}")
    private String webhookUrl;

    private final DiscordUtil discordUtil;
    private final WebClient webClient;

    public DiscordAlarmSender(
            @Qualifier("discordWebClient") WebClient webClient,
            DiscordUtil discordUtil
    ) {
        this.webClient = webClient;
        this.discordUtil = discordUtil;
    }

    /**
     * Discord로 서버 에러 알림을 비동기로 전송하는 진입 함수
     */
    public void sendDiscordAlarm(
            Exception exception,
            HttpServletRequest request
    ) {

        Object alarmBody = discordUtil.createMessage(exception, request);

        webClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(alarmBody)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        unused -> {
                        },
                        error -> log.error(
                                "[*] Error to Send Discord Alarm | Exception : {}",
                                exception.getClass().getSimpleName(),
                                error
                        ),
                        () -> log.info("[*] Success to Send Discord Alarm")
                );
    }

}