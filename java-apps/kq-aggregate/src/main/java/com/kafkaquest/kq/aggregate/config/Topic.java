package com.kafkaquest.kq.aggregate.config;

/**
 * Defines the topics used in the application
 */
public enum Topic {
    SOURCE_TOPIC_REGEX("source_topic_regex"),
    SINK_TOPIC("sink_topic");

    private final String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
