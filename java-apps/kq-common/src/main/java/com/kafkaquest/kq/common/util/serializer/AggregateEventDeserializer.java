package com.kafkaquest.kq.common.util.serializer;

import  com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.serializer.generic.GenericKafkaDeserializer;

public class AggregateEventDeserializer extends GenericKafkaDeserializer<AggregateEvent> {

    public AggregateEventDeserializer(Class<AggregateEvent> classValue) {
        super(classValue);
    }

    public AggregateEventDeserializer() {
        super(AggregateEvent.class);
    }
}
