package com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket;

public enum WebSocketTopics {
    EVENT_RESPONSE("/track"),
    NOTIFICATION_RESPONSE("/notification");

    private final String topic;

    WebSocketTopics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }
}
