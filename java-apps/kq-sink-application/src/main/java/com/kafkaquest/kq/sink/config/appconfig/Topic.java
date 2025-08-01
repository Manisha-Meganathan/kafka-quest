package com.kafkaquest.kq.sink.config.appconfig;

public enum Topic {
    SOURCE_TOPIC_REGEX("source_topic_regex");

    private final String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
