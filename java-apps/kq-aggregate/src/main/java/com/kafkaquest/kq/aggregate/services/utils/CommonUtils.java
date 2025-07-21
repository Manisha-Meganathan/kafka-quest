package com.kafkaquest.kq.aggregate.services.utils;

import com.kafkaquest.kq.aggregate.models.ConsumeEvent;
import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.events.eventdata.ExceptionalEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventStatus;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;

import java.time.LocalDateTime;

public class CommonUtils {

    public static AggregateEvent buildExceptionalAggregateEvent(ConsumeEvent event, String exceptionMessage) {
        return AggregateEvent.builder()
                .gameId(event.getGameId())
                .playerId(event.getPlayerId())
                .eventType(EventType.EXCEPTIONAL_EVENT)
                .initiatedBy(EventInitiatedBy.AGGREGATE)
                .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                .eventStatus(EventStatus.OTHER)
                .eventData(new ExceptionalEventData(event.getEventType(), exceptionMessage))
                .build();
    }
}
