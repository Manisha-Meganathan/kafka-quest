package com.kafkaquest.kq.api.KESAApiService.dto;

import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInfo {

    private UUID gameId;
    private Long playerId;
    private EventType eventType;
    private EventInitiatedBy initiatedBy;

}
