package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.enums.EventType;

import java.util.UUID;

public class Constants {

    private static final String INCORRECT_PIECE_ID = "Incorrect Id : ";
    public static final String POSITION_OCCUPIED = "Position is already occupied in the grid.";
    public static final String INCORRECT_NUMBER_OF_PIECES = "Incorrect Number of Puzzle Pieces";
    public static final String FAILED_LOADING_JIGSAW_PIECES = "An Error occurred while loading Puzzle Pieces";
    private static final String UNSUPPORTED_EVENT_TYPE = "Unsupported event type. Supporting type is: ";
    public static final String FAILED_TO_FETCH_EVENTS_FROM_DB = "An Error occurred while fetching data from database";
    public static final String FAILED_TO_MAP_DOMAIN_EVENTS = "An Error occurred while mapping domain events to aggregate events";

    public static String getIncorrectPieceIdMessage(UUID pieceId) {
        return INCORRECT_PIECE_ID + pieceId;
    }
    public static String getUnsupportedEventTypeMessage(EventType eventType) {
        return UNSUPPORTED_EVENT_TYPE + eventType.name();
    }
}
