package com.kafkaquest.kq.api.KESAApiService.config.websocket;

import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final ConcurrentHashMap<UUID, PlayerSession> activeSessions = new ConcurrentHashMap<>();

    public void addSession(UUID gameId, PlayerSession playerSession) {
        activeSessions.put(gameId, playerSession);
    }

    @Nullable
    public PlayerSession getSessionByGameId(UUID gameId) {
        return activeSessions.get(gameId);
    }

    @Nullable
    public PlayerSession getSessionByTrackingId(String trackingId) {
        final var optionalSession = getEntryByTrackingId(trackingId);
        return optionalSession.map(Map.Entry::getValue).orElse(null);
    }

    private Optional<Map.Entry<UUID, PlayerSession>> getEntryByTrackingId(String trackingId) {
        return activeSessions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getTrackingId().equals(trackingId))
                .findFirst();
    }

    private Optional<Map.Entry<UUID, PlayerSession>> getEntryBySessionId(String sessionId) {
        return activeSessions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getSessionId().equals(sessionId))
                .findFirst();
    }

    public void updateDisconnectionStatus(String sessionId, boolean isDisconnected) {
        final var optionalSession = getEntryBySessionId(sessionId);
        optionalSession.ifPresent(
            uuidPlayerSessionEntry -> {
                PlayerSession playerSession = uuidPlayerSessionEntry.getValue();
                playerSession.setDisconnected(isDisconnected);
                playerSession.setExpiryTime(LocalDateTime.now().plusMinutes(10));
            }
        );
    }

    public ConcurrentHashMap<UUID, PlayerSession> getActiveSessions() {
        return activeSessions;
    }
}
