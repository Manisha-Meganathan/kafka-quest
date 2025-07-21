package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.events.eventdata.EventData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A DTO event class used as the input event type in the Aggregate Service
 * Note: eventStreamId is null when an event is consumed from API Service
 * Note: eventStreamId should not be null when mapped from a Domain event
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeEvent {

    private UUID gameId;

    private Long playerId;

    private Integer eventStreamId;

    private Integer revertFromEventStreamId;

    private EventType eventType;

    private EventInitiatedBy initiatedBy;

    private Long timestamp;

    private EventData eventData;
}
