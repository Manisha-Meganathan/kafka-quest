package com.kafkaquest.kq.aggregate.services.handlers;

import com.kafkaquest.kq.aggregate.models.ConsumeEvent;
import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.models.JigsawPuzzleGame;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzleGameManager;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzlePiecesManager;
import com.kafkaquest.kq.common.util.models.events.eventdata.GameStartedEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;
import io.vavr.control.Try;

import java.util.List;
import java.util.Objects;

import static com.kafkaquest.kq.aggregate.models.Constants.FAILED_LOADING_JIGSAW_PIECES;
import static com.kafkaquest.kq.aggregate.models.Constants.getUnsupportedEventTypeMessage;
import static com.kafkaquest.kq.aggregate.services.utils.CommonUtils.buildExceptionalAggregateEvent;

public class GameStartedEventHandler implements EventHandler {

    @Override
    public List<HandlerResult> handle(byte[] event, JigsawPuzzleGameManager manager) {
        final var consumedEvent = deserialize(event, GameStartedEventData.class);

        final var result = handle(consumedEvent, manager);

        return List.of(result);
    }

    public HandlerResult handle(ConsumeEvent consumedEvent, JigsawPuzzleGameManager manager) {

        final GameStartedEventData eventData = (GameStartedEventData) consumedEvent.getEventData();

        if (validateConsumedEvent(consumedEvent)) {

            var exceptionalEvent = buildExceptionalAggregateEvent(
                    consumedEvent,
                    getUnsupportedEventTypeMessage(consumedEvent.getEventType())
            );
            return new HandlerResult(getExceptionalEventKey(), exceptionalEvent);
        }

        final var aggregate = new JigsawPuzzleGame();

        final var pieceManager = JigsawPuzzlePiecesManager.getInstance();
        final var result = Try.of(() -> pieceManager.mapToPuzzlePieces(eventData.getVerticalSize(), eventData.getHorizontalSize()));


        if (result.isFailure()) {

            var exceptionalEvent = buildExceptionalAggregateEvent(
                    consumedEvent,
                    FAILED_LOADING_JIGSAW_PIECES
            );
            return new HandlerResult(getExceptionalEventKey(), exceptionalEvent);
        }

        final var aggregateEvent = aggregate.create(consumedEvent, result.get());

        final HandlerResult handlerResult;

        if (aggregateEvent.getEventType() == EventType.EXCEPTIONAL_EVENT) {

            handlerResult = new HandlerResult(getExceptionalEventKey(), aggregateEvent);
        } else {

            handlerResult = new HandlerResult(getKey(), aggregateEvent);
            manager.addAggregate(aggregate);
        }

        return handlerResult;
    }

    private boolean validateConsumedEvent(ConsumeEvent consumeEvent) {
        return Objects.isNull(consumeEvent)
                || consumeEvent.getEventType() != EventType.GAME_STARTED_EVENT
                || !(consumeEvent.getEventData() instanceof GameStartedEventData);
    }

    public static AggregateKey getKey() {
        return new AggregateKey(EventType.GAME_STARTED_EVENT);
    }
}
