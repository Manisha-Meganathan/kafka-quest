package com.kafkaquest.kq.common.util.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaquest.kq.common.util.models.events.DomainEvent;

public interface CommonUtils {

    Logger log = LoggerFactory.getLogger(CommonUtils.class);

    static DomainEvent deserializeAggregateEventToDomainEvent(byte[] eventAsBytes) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(eventAsBytes, DomainEvent.class);
        } catch (Exception ex) {
            log.warn("An Error occurred while deserializing");
            throw new RuntimeException("Failed to deserialize value", ex);
        }
    }

}
