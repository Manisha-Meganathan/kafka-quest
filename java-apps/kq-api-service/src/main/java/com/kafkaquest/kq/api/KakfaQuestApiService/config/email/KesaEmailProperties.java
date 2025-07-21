package com.kafkaquest.kq.api.KESAApiService.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail.kesa")
@ConfigurationPropertiesScan
@Component
public class KesaEmailProperties {

    private String subject;

    private String sender;

    private String senderTitle;

    private String[] recipients;
}
