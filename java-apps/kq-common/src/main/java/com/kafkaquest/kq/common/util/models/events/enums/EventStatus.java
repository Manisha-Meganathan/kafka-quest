package com.kafkaquest.kq.common.util.models.events.enums;

public enum EventStatus {

    GAME_ENDED(0),
    GAME_PAUSED(1),
    GAME_RESUMED(2),
    OTHER(3);

    private final int eventStatusNumber;

    EventStatus(int eventStatusNumber) {
        this.eventStatusNumber = eventStatusNumber;
    }

    public int getEventStatusNumber() {
        return eventStatusNumber;
    }

}
