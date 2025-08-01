package com.kafkaquest.kq.common.util.models.events.enums;

public enum EventType {

    GAME_STARTED_EVENT(0),
    PIECE_ADDED_EVENT(1),
    PIECE_REMOVED_EVENT(2),
    REVERT_MOVES_EVENT(3),
    SAVE_CURRENT_STATE_EVENT(4),
    STARTED_FROM_SAVED_STATE_EVENT(5),
    GAME_PAUSED_EVENT(6),
    GAME_RESUMED_EVENT(7),
    EXCEPTIONAL_EVENT(8),
    DISCARD_GAME_EVENT(9);

    private final int eventNumber;

    EventType(int eventNumber) {
        this.eventNumber = eventNumber;
    }

    public int getEventNumber() {
        return eventNumber;
    }

}
