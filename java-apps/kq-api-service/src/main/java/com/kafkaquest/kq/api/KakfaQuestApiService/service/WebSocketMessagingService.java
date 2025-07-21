package com.kafkaquest.kq.api.KafkaQuestApiService.service;

import com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket.PlayerSession;
import com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket.WebSocketSessionManager;
import com.kafkaquest.kq.api.KafkaQuestApiService.dto.Notification;
import com.kafkaquest.kq.common.util.models.events.ProjectionEventResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WebSocketMessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    private final WebSocketSessionManager sessionManager;

    private final UndeliveredResponsesManager undeliveredResponsesManager;

    public void sendMessage(String trackingId, String destination, ProjectionEventResponse response) {
        messagingTemplate.convertAndSendToUser(trackingId, destination, response);
    }

    public void sendMessage(String trackingId, String destination, Notification notification) {
        messagingTemplate.convertAndSendToUser(trackingId, destination, notification);
    }
    
    public void sendMessage(UUID gameId, String destination, ProjectionEventResponse response) {
        final PlayerSession playerSession = sessionManager.getSessionByGameId(gameId);

        if (playerSession == null) {
            return;
        }

        if (playerSession.isDisconnected()) {
            undeliveredResponsesManager.addOne(playerSession.getGameId(), response);
            return;
        }

        sendMessage(playerSession.getTrackingId(), destination, response);
    }

    public void sendMessages(String trackingId, String destination, List<ProjectionEventResponse> responses) {
        responses.forEach(response -> sendMessage(trackingId, destination, response));
    }
}
