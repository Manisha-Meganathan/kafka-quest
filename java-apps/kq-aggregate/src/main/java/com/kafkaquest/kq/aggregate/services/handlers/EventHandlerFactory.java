package com.kafkaquest.kq.aggregate.services.handlers;

import com.kafkaquest.kq.common.util.models.kafkakeys.ApiKey;

public class EventHandlerFactory {

    public static EventHandler getHandler(ApiKey apiKey) {

        return switch (apiKey.getEventType()) {
            case GAME_STARTED_EVENT -> new GameStartedEventHandler();
            case PIECE_ADDED_EVENT -> new PieceAddedEventHandler();
            case PIECE_REMOVED_EVENT -> new PieceRemovedEventHandler();
            case REVERT_MOVES_EVENT -> new RevertMovesEventHandler();
            case DISCARD_GAME_EVENT -> new DiscardGameEventHandler();
            default -> throw new IllegalArgumentException("Unknown Event");
        };
    }
}
