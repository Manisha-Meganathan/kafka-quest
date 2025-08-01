package com.kafkaquest.kq.sink.config.appconfig;


import com.kafkaquest.kq.common.util.config.KafkaPropertyKey;

public enum SinkApplicationKafkaPropertyKey implements KafkaPropertyKey {
    KAFKA_BOOTSTRAP_SERVERS("bootstrap.servers"),
    KAFKA_APPLICATION_ID("application.id"),
    KAFKA_MAX_PARTITION_FETCH_BYTES("max.partition.fetch.bytes");

    private final String key;

    SinkApplicationKafkaPropertyKey(String key) {
        this.key = key;
    }

    @Override
    public String getKafkaPropertyKey() {
        return this.key;
    }
}
