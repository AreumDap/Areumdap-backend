package com.umc9th.areumdap.common.log.discord;

import com.umc9th.areumdap.common.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DiscordLoggerAop {

    private final DiscordAlarmSender discordAlarmSender;

    /**
     * General Exception 처리 지점
     */
    @Pointcut("execution(* com.umc9th.areumdap.common.exception.GeneralExceptionAdvice.handleGeneralException(..))")
    public void generalExceptionErrorLoggerExecute() {
    }

    /**
     * Exception (500) 처리 지점
     */
    @Pointcut("execution(* com.umc9th.areumdap.common.exception.GeneralExceptionAdvice.handleException(..))")
    public void serverExceptionErrorLoggerExecute() {
    }

    /**
     * GeneralException 중 500만 Discord 알림
     */
    @Before("generalExceptionErrorLoggerExecute()")
    public void generalExceptionLogging(JoinPoint joinPoint) {
        HttpServletRequest request = getCurrentRequest();
        GeneralException exception = (GeneralException) joinPoint.getArgs()[0];

        if (exception.getErrorStatus().getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR)
            discordAlarmSender.sendDiscordAlarm(exception, request);
    }

    /**
     * 그 외 서버 예외 (Exception)
     */
    @Before("serverExceptionErrorLoggerExecute()")
    public void serverExceptionLogging(JoinPoint joinPoint) {
        HttpServletRequest request = getCurrentRequest();
        Exception exception = (Exception) joinPoint.getArgs()[0];

        discordAlarmSender.sendDiscordAlarm(exception, request);
    }

    /**
     * 현재 요청 스레드에 바인딩된 HttpServletRequest를 가져오는 유틸 함수
     */
    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()
        ).getRequest();
    }

}
