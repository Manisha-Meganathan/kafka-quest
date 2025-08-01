package com.kafkaquest.kq.common.util.models.kafkakeys;

import java.util.UUID;

import com.kafkaquest.kq.common.util.models.events.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionKey {
    private Long playerId;
    private UUID gameId;
    private EventType eventType;
}
