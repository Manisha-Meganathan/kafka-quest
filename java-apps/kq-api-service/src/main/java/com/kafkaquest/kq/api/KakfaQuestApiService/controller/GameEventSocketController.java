package com.kafkaquest.kq.api.KESAApiService.controller;

import com.kafkaquest.kq.api.KESAApiService.config.websocket.PlayerSession;
import com.kafkaquest.kq.api.KESAApiService.config.websocket.WebSocketSessionManager;
import com.kafkaquest.kq.api.KESAApiService.dto.Notification;
import com.kafkaquest.kq.api.KESAApiService.dto.UIEvent;
import com.kafkaquest.kq.api.KESAApiService.kafka.EventProducer;
import com.kafkaquest.kq.api.KESAApiService.service.UndeliveredResponsesManager;
import com.kafkaquest.kq.api.KESAApiService.service.WebSocketMessagingService;
import com.kafkaquest.kq.common.util.models.events.Event;
import com.kafkaquest.kq.common.util.models.events.ProjectionEventResponse;
import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.kafkaquest.kq.api.KESAApiService.config.websocket.PlayerSessionKeys.PLAYER_ID;
import static com.kafkaquest.kq.api.KESAApiService.config.websocket.PlayerSessionKeys.TRACKING_ID;
import static com.kafkaquest.kq.api.KESAApiService.config.websocket.WebSocketTopics.EVENT_RESPONSE;
import static com.kafkaquest.kq.api.KESAApiService.config.websocket.WebSocketTopics.NOTIFICATION_RESPONSE;

@Controller
@AllArgsConstructor
@Slf4j(topic = "[GameEventSocketController]")
public class GameEventSocketController {

    private final EventProducer eventProducer;

    private final WebSocketSessionManager sessionManager;

    private final UndeliveredResponsesManager undeliveredResponsesManager;

    private final WebSocketMessagingService messagingService;

    @MessageMapping("/event/start-game")
    public void produceGameStartedEvent(
        @Payload UIEvent gameStartedEvent,
        SimpMessageHeaderAccessor headerAccessor
    ) {
        final UUID gameId = UUID.randomUUID();
        final String playerIdHeaderValue = headerAccessor.getFirstNativeHeader(PLAYER_ID.getKey()).toString();
        final String trackingId = headerAccessor.getFirstNativeHeader(TRACKING_ID.getKey()).toString();
        final Long playerId = Long.parseLong(playerIdHeaderValue);
        final String sessionId = headerAccessor.getSessionId();

        final PlayerSession playerSession = PlayerSession.builder()
                .playerId(playerId)
                .gameId(gameId)
                .trackingId(trackingId)
                .sessionId(sessionId)
                .build();

        sessionManager.addSession(gameId, playerSession);

        Event gameEvent = Event.builder()
                .gameId(gameId)
                .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                .eventType(gameStartedEvent.getEventInfo().getEventType())
                .initiatedBy(gameStartedEvent.getEventInfo().getInitiatedBy())
                .playerId(gameStartedEvent.getEventInfo().getPlayerId())
                .eventData(gameStartedEvent.getEventData())
                .build();

        ApiKey apiKey = ApiKey.builder()
                .eventType(gameStartedEvent.getEventInfo().getEventType())
                .build();

        eventProducer.sendEventMessage(apiKey, gameEvent);
    }

    @MessageMapping("/event")
    public void produceEvent(@Payload UIEvent event) {
        Event gameEvent = Event.builder()
                .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                .eventType(event.getEventInfo().getEventType())
                .initiatedBy(event.getEventInfo().getInitiatedBy())
                .playerId(event.getEventInfo().getPlayerId())
                .eventData(event.getEventData())
                .gameId(event.getEventInfo().getGameId())
                .build();

        ApiKey apiKey = ApiKey.builder()
                .eventType(event.getEventInfo().getEventType())
                .build();

        eventProducer.sendEventMessage(apiKey, gameEvent);
    }

    @MessageMapping("/trigger-undelivered-response-delivery")
    public void triggerUndeliveredResponsesDelivery(
        SimpMessageHeaderAccessor headerAccessor
    ) {
        final String playerIdHeaderValue = headerAccessor.getFirstNativeHeader(PLAYER_ID.getKey()).toString();
        final String trackingId = headerAccessor.getFirstNativeHeader(TRACKING_ID.getKey()).toString();
        final Long playerId = Long.parseLong(playerIdHeaderValue);
        final String sessionId = headerAccessor.getSessionId();

        final PlayerSession session = sessionManager.getSessionByTrackingId(trackingId);

        if (session == null) {
            messagingService.sendMessage(trackingId, NOTIFICATION_RESPONSE.getTopic(), Notification.NO_MESSAGES_TO_DELIVER);
            return;
        }

        session.setSessionId(sessionId);
        session.setDisconnected(false);

        final List<ProjectionEventResponse> responses =
                undeliveredResponsesManager.getUndeliveredResponses(session.getGameId());

        if (responses == null) {
            messagingService.sendMessage(trackingId, NOTIFICATION_RESPONSE.getTopic(), Notification.NO_MESSAGES_TO_DELIVER);
            return;
        }

        messagingService.sendMessage(trackingId, NOTIFICATION_RESPONSE.getTopic(), Notification.UNDELIVERED_RESPONSES_DELIVERY_STARTED);
        messagingService.sendMessages(trackingId, EVENT_RESPONSE.getTopic(), responses);
        messagingService.sendMessage(trackingId, NOTIFICATION_RESPONSE.getTopic(), Notification.UNDELIVERED_RESPONSES_DELIVERY_COMPLETED);
        undeliveredResponsesManager.removeUndeliveredResponses(session.getGameId());
    }
}