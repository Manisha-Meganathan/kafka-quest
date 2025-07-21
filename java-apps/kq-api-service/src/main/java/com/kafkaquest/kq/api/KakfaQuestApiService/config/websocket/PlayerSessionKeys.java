package com.kafkaquest.kq.api.KESAApiService.config.websocket;

public enum PlayerSessionKeys {
    PLAYER_ID("player-id"),

    TRACKING_ID("tracking-id"),

    GAME_ID("game-id"),

    SESSION_ID("session-id");

    private final String key;

    PlayerSessionKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
