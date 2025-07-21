package com.kafkaquest.kq.common.util.models.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kafkaquest.kq.common.util.models.events.enums.EventStatus;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.utils.EventDataDeserializer;
import com.kafkaquest.kq.common.util.utils.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainEvent {

    private UUID gameId;

    private Long playerId;

    private Integer eventStreamId;

    private EventType eventType;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    private EventStatus eventStatus;

    @JsonDeserialize(using = EventDataDeserializer.class)
    private byte[] eventData;
}
