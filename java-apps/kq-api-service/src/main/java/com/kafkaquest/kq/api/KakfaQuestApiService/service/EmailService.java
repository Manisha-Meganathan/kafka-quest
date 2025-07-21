package com.kafkaquest.kq.api.KESAApiService.service;

import com.kafkaquest.kq.api.KESAApiService.config.email.KesaEmailProperties;
import com.kafkaquest.kq.api.KESAApiService.dto.DemoRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j(topic = "[EmailService]")
public class EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    private final KesaEmailProperties kesaEmailProperties;

    private static final String KESA_DEMO_TEMPLATE = "kesa_demo";

    public void sendDemoRequestEmail(
        DemoRequest demoRequest
    ) throws MessagingException, UnsupportedEncodingException {
        log.info("Start Sending Demo Request Email");

        this.sendEmail(
            kesaEmailProperties.getSender(),
            kesaEmailProperties.getSenderTitle(),
            kesaEmailProperties.getRecipients(),
            kesaEmailProperties.getSubject(),
            demoRequest.getEmailTemplateKeyValues(),
            KESA_DEMO_TEMPLATE
        );
        log.info("Completed Sending Demo Request Email");
    }

    public void sendEmail(
        String sender,
        String senderTitle,
        String[] recipients,
        String subject,
        Map<String, Object> templateModel,
        String template
    ) throws MessagingException, UnsupportedEncodingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = templateEngine.process(template, thymeleafContext);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(sender, senderTitle);
        helper.setTo(recipients);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
}
