package com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private final WebSocketSessionManager sessionManager;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        final String sessionId = event.getSessionId();
        sessionManager.updateDisconnectionStatus(sessionId, true);
    }
}
