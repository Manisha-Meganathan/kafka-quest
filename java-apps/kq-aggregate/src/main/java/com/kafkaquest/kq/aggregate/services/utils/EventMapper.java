package com.kafkaquest.kq.aggregate.services.utils;

import com.kafkaquest.kq.aggregate.models.ConsumeEvent;
import com.kafkaquest.kq.common.util.models.events.DomainEvent;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

import static com.kafkaquest.kq.common.util.mapper.EventMapper.getEventDataClass;
import static com.kafkaquest.kq.common.util.mapper.EventMapper.parseEventData;

public interface EventMapper {

    static List<ConsumeEvent> fromDboToConsumeEvent(List<DomainEvent> domainEvents, EventInitiatedBy initiatedBy) {
        return domainEvents.stream()
                .map(domainEvent -> fromDboToConsumeEvent(domainEvent, initiatedBy))
                .collect(Collectors.toList());
    }

    static ConsumeEvent fromDboToConsumeEvent(DomainEvent domainEvent, EventInitiatedBy initiatedBy) {

        final var eventDataType = getEventDataClass(domainEvent.getEventType());

        return ConsumeEvent.builder()
                .gameId(domainEvent.getGameId())
                .playerId(domainEvent.getPlayerId())
                .eventStreamId(domainEvent.getEventStreamId())
                .eventType(domainEvent.getEventType())
                .initiatedBy(initiatedBy)
                .timestamp(DateTimeUtil.dateTimeToEpoch(domainEvent.getTimestamp()))
                .eventData(parseEventData(domainEvent.getEventData(), eventDataType))
                .build();
    }
}
