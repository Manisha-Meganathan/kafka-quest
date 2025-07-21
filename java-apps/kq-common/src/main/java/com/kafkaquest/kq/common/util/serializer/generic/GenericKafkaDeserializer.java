package com.kafkaquest.kq.common.util.serializer.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafkaquest.kq.common.util.exceptions.DeserializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j(topic = "[GenericKafkaDeserializer]")
public class GenericKafkaDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> classValue;

    public GenericKafkaDeserializer(Class<T> classValue) {
        this.classValue = classValue;
    }

    public GenericKafkaDeserializer() {
        classValue = null;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.info("Null received at deserializing");
                return null;
            }
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(data, classValue);
        } catch (Exception e) {
            log.error("An Exception occurred while deserializing");
            throw new DeserializationException("Error when deserializing byte[] " + e.getMessage());
        }
    }

}
