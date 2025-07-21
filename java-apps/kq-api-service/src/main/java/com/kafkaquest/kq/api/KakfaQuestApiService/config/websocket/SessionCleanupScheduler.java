package com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaquest.kq.api.KafkaQuestApiService.kafka.EventProducer;
import com.kafkaquest.kq.api.KafkaQuestApiService.service.UndeliveredResponsesManager;
import com.kafkaquest.kq.common.util.models.events.Event;
import com.kafkaquest.kq.common.util.models.events.eventdata.GameDiscardedEventData;
import com.kafkaquest.kq.common.util.models.events.enums.DiscardReason;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class SessionCleanupScheduler {

    private final WebSocketSessionManager sessionManager;

    private final UndeliveredResponsesManager undeliveredResponsesManager;

    private final EventProducer eventProducer;

    private static JsonNode cachedDiscardEventData = null;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void cleanupExpiredSessions() {
        final var sessionMap = sessionManager.getActiveSessions();

        Iterator<Map.Entry<UUID, PlayerSession>> iterator = sessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, PlayerSession> entry = iterator.next();
            PlayerSession session = entry.getValue();
            if (session.isDisconnected() && session.getExpiryTime().isBefore(LocalDateTime.now())) {
                iterator.remove();
                undeliveredResponsesManager.removeUndeliveredResponses(session.getGameId());
                Event discardGameEvent = Event.builder()
                        .gameId(session.getGameId())
                        .playerId(session.getPlayerId())
                        .initiatedBy(EventInitiatedBy.API)
                        .eventType(EventType.DISCARD_GAME_EVENT)
                        .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                        .eventData(getDiscardEventDataAsJson())
                        .build();

                eventProducer.sendEventMessage(new ApiKey(EventType.DISCARD_GAME_EVENT), discardGameEvent);
            }
        }
    }

    private JsonNode getDiscardEventDataAsJson() {
        if (cachedDiscardEventData != null) {
            return cachedDiscardEventData;
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        final GameDiscardedEventData discardEventData = new GameDiscardedEventData(DiscardReason.CLIENT_DISCONTINUED);

        JsonNode jsonNode = objectMapper.valueToTree(discardEventData);
        cachedDiscardEventData = jsonNode;
        return jsonNode;
    }

}
