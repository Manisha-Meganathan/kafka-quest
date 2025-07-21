package com.kafkaquest.kq.common.util.serializer.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j(topic = "[GenericKafkaSerializer]")
public class GenericKafkaSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            if (data == null){
                log.info("Null received at serializing");
                return null;
            }
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("An Exception occurred while serializing");
            throw new SerializationException("Error when serializing to byte[] " + e.getMessage());
        }
    }

}
