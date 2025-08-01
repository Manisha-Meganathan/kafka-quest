package com.kafkaquest.kq.common.util.models.events;

import com.kafkaquest.kq.common.util.models.events.eventdata.EventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventStatus;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * This class represents an Event produced to kafka from Aggregate Service
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateEvent {

    private UUID gameId;

    private Long playerId;

    private Integer eventStreamId;

    private Integer revertFromEventStreamId;

    private EventType eventType;

    private EventInitiatedBy initiatedBy;

    private long timestamp;

    private EventStatus eventStatus;

    private EventData eventData;

}
