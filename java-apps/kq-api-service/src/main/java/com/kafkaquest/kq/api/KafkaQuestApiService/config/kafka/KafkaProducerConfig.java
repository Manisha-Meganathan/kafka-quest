package com.kafkaquest.kq.api.KafkaQuestApiService.config.kafka;

import com.kafkaquest.kq.common.util.models.events.Event;
import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import com.kafkaquest.kq.common.util.serializer.ApiKeySerializer;
import com.kafkaquest.kq.common.util.serializer.EventSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    @Bean
    public ProducerFactory<ApiKey, Event> eventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress
        );
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                ApiKeySerializer.class
        );
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                EventSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<ApiKey, Event> kafkaEventTemplate() {
        return new KafkaTemplate<>(eventProducerFactory());
    }
}
