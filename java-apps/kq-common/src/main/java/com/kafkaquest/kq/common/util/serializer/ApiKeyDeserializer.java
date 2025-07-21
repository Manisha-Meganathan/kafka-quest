package com.kafkaquest.kq.common.util.serializer;

import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import com.kafkaquest.kq.common.util.serializer.generic.GenericKafkaDeserializer;

public class ApiKeyDeserializer extends GenericKafkaDeserializer<ApiKey> {
    public ApiKeyDeserializer(Class<ApiKey> classValue) {
        super(classValue);
    }

    public ApiKeyDeserializer() {
        super(ApiKey.class);
    }
}
