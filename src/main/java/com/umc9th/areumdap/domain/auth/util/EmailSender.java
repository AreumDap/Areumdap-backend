package com.umc9th.areumdap.domain.auth.util;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value(value = "${spring.mail.sender-address}")
    private String senderAddress;

    // 이메일 인증 코드 발송
    public void sendEmailVerificationCode(
            String requestEmail, String verificationCode
    ){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderAddress);
            helper.setTo(requestEmail);
            helper.setSubject("[Areumdap] 이메일 인증 코드입니다.");
            helper.setText(buildHtmlContent(verificationCode), true);

            mailSender.send(message);

            log.info("이메일 전송 완료 -> {}", requestEmail);
        }
        catch (Exception e) {
            log.error("이메일 전송 실패 → {}", requestEmail, e);
            throw new GeneralException(ErrorStatus.SEND_VERIFICATION_CODE_EMAIL_INTERNAL_SERVER_ERROR);
        }
    }

    // HTML 템플릿 구성
    private String buildHtmlContent(String code) {
        Context context = new Context();
        context.setVariable("verificationCode", code);
        return templateEngine.process("mail/verificationEmail", context);
    }

}
