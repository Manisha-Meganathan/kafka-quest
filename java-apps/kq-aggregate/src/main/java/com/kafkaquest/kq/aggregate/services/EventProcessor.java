package com.kafkaquest.kq.aggregate.services;

import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.services.handlers.EventHandlerFactory;
import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;
import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import org.apache.kafka.streams.KeyValue;

import java.util.List;
import java.util.stream.Collectors;

public class EventProcessor {

    public static Iterable<KeyValue<AggregateKey, AggregateEvent>> process(ApiKey key, byte[] value) {

        final var manager = JigsawPuzzleGameManager.getInstance();

        final var eventHandler = EventHandlerFactory.getHandler(key);

        final var handlerResults = eventHandler.handle(value, manager);

        return map(handlerResults);
    }

    private static Iterable<KeyValue<AggregateKey, AggregateEvent>> map(List<HandlerResult> handlerResults) {
        return handlerResults.stream()
                .map(EventProcessor::toKeyValuePair)
                .collect(Collectors.toList());
    }

    private static KeyValue<AggregateKey, AggregateEvent> toKeyValuePair(HandlerResult handlerResult) {
        return new KeyValue<>(handlerResult.getAggregateKey(), handlerResult.getAggregateEvent());
    }
}
