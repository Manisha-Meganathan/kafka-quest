package com.kafkaquest.kq.aggregate.config;


import com.kafkaquest.kq.common.util.config.KafkaPropertyKey;

public enum AggregateKafkaPropertyKey implements KafkaPropertyKey {
    KAFKA_BOOTSTRAP_SERVERS("bootstrap.servers"),
    KAFKA_APPLICATION_ID("application.id"),
    KAFKA_MAX_REQUEST_SIZE("max.request.size");

    private final String key;

    AggregateKafkaPropertyKey(String key) {
        this.key = key;
    }

    @Override
    public String getKafkaPropertyKey() {
        return this.key;
    }
}
