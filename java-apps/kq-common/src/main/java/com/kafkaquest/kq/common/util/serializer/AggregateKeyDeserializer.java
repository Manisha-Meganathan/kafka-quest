package com.kafkaquest.kq.common.util.serializer;

import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;
import com.kafkaquest.kq.common.util.serializer.generic.GenericKafkaDeserializer;

public class AggregateKeyDeserializer extends GenericKafkaDeserializer<AggregateKey> {
    public AggregateKeyDeserializer(Class<AggregateKey> classValue) {
        super(classValue);
    }

    public AggregateKeyDeserializer() {
        super(AggregateKey.class);
    }
}
