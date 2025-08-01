package com.kafkaquest.kq.aggregate.services.handlers;

import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzleGameManager;
import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.events.eventdata.GameDiscardedEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventStatus;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;

import java.util.List;

public class DiscardGameEventHandler implements EventHandler {

    @Override
    public List<HandlerResult> handle(byte[] event, JigsawPuzzleGameManager manager) {
        final var consumedEvent = deserialize(event, GameDiscardedEventData.class);
        manager.removeAggregate(consumedEvent.getGameId());

        final AggregateEvent discardedEvent = AggregateEvent.builder()
                .gameId(consumedEvent.getGameId())
                .playerId(consumedEvent.getPlayerId())
                .eventType(consumedEvent.getEventType())
                .initiatedBy(consumedEvent.getInitiatedBy())
                .timestamp(consumedEvent.getTimestamp())
                .eventStatus(EventStatus.OTHER)
                .eventData(consumedEvent.getEventData())
                .build();

        return List.of(new HandlerResult(getKey(), discardedEvent));
    }

    public static AggregateKey getKey() {
        return new AggregateKey(EventType.DISCARD_GAME_EVENT);
    }
}
