package com.kafkaquest.kq.common.util.models.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * This class represents an Event produced to kafka from API Service
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private UUID gameId;

    private Long playerId;

    private EventType eventType;

    private EventInitiatedBy initiatedBy;

    private long timestamp;

    private JsonNode eventData;

}
