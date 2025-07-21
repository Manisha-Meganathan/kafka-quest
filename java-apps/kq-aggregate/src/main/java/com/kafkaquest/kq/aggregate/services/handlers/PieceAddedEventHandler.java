package com.kafkaquest.kq.aggregate.services.handlers;

import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzleGameManager;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceAddedEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;

import java.util.List;

public class PieceAddedEventHandler implements EventHandler {

    @Override
    public List<HandlerResult> handle(byte[] event, JigsawPuzzleGameManager manager) {
        final var consumedEvent = deserialize(event, PieceAddedEventData.class);

        final var aggregate = manager.getAggregate(consumedEvent.getGameId());

        final var aggregateEvent = aggregate.addPuzzlePiece(consumedEvent);

        final HandlerResult handlerResult;

        if (aggregateEvent.getEventType() == EventType.EXCEPTIONAL_EVENT) {

            handlerResult = new HandlerResult(getExceptionalEventKey(), aggregateEvent);
        } else {
            handlerResult = new HandlerResult(getKey(), aggregateEvent);
        }

        return List.of(handlerResult);
    }

    public static AggregateKey getKey() {
        return new AggregateKey(EventType.PIECE_ADDED_EVENT);
    }
}
