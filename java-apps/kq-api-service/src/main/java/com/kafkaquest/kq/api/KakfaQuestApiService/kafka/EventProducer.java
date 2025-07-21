package com.kafkaquest.kq.api.KESAApiService.kafka;

import com.kafkaquest.kq.common.util.models.events.Event;
import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "[EventProducer]")
public class EventProducer {

    @Value("${spring.kafka.topics.producer-topic}")
    private String topic;

    @NonNull
    private final KafkaTemplate<ApiKey, Event> kafkaEventTemplate;

    public void sendEventMessage(ApiKey apiKey, Event event) {
        this.kafkaEventTemplate.send(topic, apiKey, event);
        log.info("event type " + apiKey.getEventType().getEventNumber() + " event " + event.getEventData().asText());
        log.info("Game Event Published");
    }
}
