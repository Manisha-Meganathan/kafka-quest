package com.kafkaquest.kq.common.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaquest.kq.common.util.exceptions.DeserializationException;
import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.events.DomainEvent;
import com.kafkaquest.kq.common.util.models.events.eventdata.EventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.ExceptionalEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.GameStartedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceAddedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceRemovedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.RevertMovesEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public interface EventMapper {

    Logger log = LoggerFactory.getLogger(EventMapper.class);

    static List<AggregateEvent> fromDboToAggregateEvent(List<DomainEvent> domainEvents, EventInitiatedBy initiatedBy) {
        return domainEvents.stream()
                .map(domainEvent -> fromDboToAggregateEvent(domainEvent, initiatedBy))
                .collect(Collectors.toList());
    }

    static AggregateEvent fromDboToAggregateEvent(DomainEvent domainEvent, EventInitiatedBy initiatedBy) {

        final var eventDataType = getEventDataClass(domainEvent.getEventType());

        return AggregateEvent.builder()
                .gameId(domainEvent.getGameId())
                .playerId(domainEvent.getPlayerId())
                .eventStreamId(domainEvent.getEventStreamId())
                .eventType(domainEvent.getEventType())
                .initiatedBy(initiatedBy)
                .timestamp(DateTimeUtil.dateTimeToEpoch(domainEvent.getTimestamp()))
                .eventStatus(domainEvent.getEventStatus())
                .eventData(parseEventData(domainEvent.getEventData(), eventDataType))
                .build();
    }

    static Class<? extends EventData> getEventDataClass(EventType eventType) {
        return switch (eventType) {
            case GAME_STARTED_EVENT -> GameStartedEventData.class;
            case PIECE_ADDED_EVENT -> PieceAddedEventData.class;
            case PIECE_REMOVED_EVENT -> PieceRemovedEventData.class;
            case EXCEPTIONAL_EVENT -> ExceptionalEventData.class;
            case REVERT_MOVES_EVENT -> RevertMovesEventData.class;
            default -> throw new IllegalArgumentException("Unknown Event");
        };
    }

    static <T extends EventData> T parseEventData(byte[] eventData, Class<T> eventDataClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(eventData, eventDataClass);
        } catch (Exception ex) {
            log.warn("[EventMapper] An exception occurred while parsing event data");
            throw new DeserializationException("An Error occurred while parsing event data");
        }
    }

}
