package com.kafkaquest.kq.api.KESAApiService.config.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PlayerSession {
    private Long playerId;
    private UUID gameId;
    private String trackingId;
    private String sessionId;
    private boolean isDisconnected;
    private LocalDateTime expiryTime;
}
