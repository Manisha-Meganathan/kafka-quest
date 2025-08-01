package com.kafkaquest.kq.common.util.serializer;

import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.serializer.generic.GenericKafkaSerializer;

public class AggregateEventSerializer extends GenericKafkaSerializer<AggregateEvent> {
}
