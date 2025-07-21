package com.kafkaquest.kq.api.KafkaQuestApiService.controller;

import com.kafkaquest.kq.api.KafkaQuestApiService.dto.DemoRequest;
import com.kafkaquest.kq.api.KafkaQuestApiService.exception.EmailDeliveryException;
import com.kafkaquest.kq.api.KafkaQuestApiService.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @CrossOrigin(origins = "*")
    @PostMapping("/send-demo-request")
    public void sendDemoRequestMail(@RequestBody @Valid DemoRequest demoRequest) {
        try {
            emailService.sendDemoRequestEmail(demoRequest);
        } catch (Exception ex) {
            throw new EmailDeliveryException("Failed Sending Demo Request Email", ex);
        }
    }

}
