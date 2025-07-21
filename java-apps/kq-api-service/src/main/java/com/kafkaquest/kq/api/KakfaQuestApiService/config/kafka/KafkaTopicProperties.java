package com.kafkaquest.kq.api.KESAApiService.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka")
@ConfigurationPropertiesScan
@Component
public class KafkaTopicProperties {

    private Map<String, String> topics;
}
