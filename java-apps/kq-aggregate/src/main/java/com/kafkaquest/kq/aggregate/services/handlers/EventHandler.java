package com.kafkaquest.kq.aggregate.services.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafkaquest.kq.aggregate.models.ConsumeEvent;
import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzleGameManager;
import com.kafkaquest.kq.common.util.models.events.eventdata.EventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;

import java.util.List;

public interface EventHandler {

    List<HandlerResult> handle(byte[] event, JigsawPuzzleGameManager manager);

    default ConsumeEvent deserialize(byte[] eventAsBytes, Class<? extends EventData> eventDataClass) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            SimpleModule module = new SimpleModule();
            module.addAbstractTypeMapping(EventData.class, eventDataClass);
            objectMapper.registerModule(module);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(eventAsBytes, ConsumeEvent.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to deserialize value", ex);
        }
    }

    default AggregateKey getExceptionalEventKey() {
        return new AggregateKey(EventType.EXCEPTIONAL_EVENT);
    }
}
