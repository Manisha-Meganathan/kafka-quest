package com.kafkaquest.kq.common.util.models.events;

import com.kafkaquest.kq.common.util.models.events.responsedata.ResponseData;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.events.enums.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionEventResponse {

    private Long playerId;
    private UUID gameId;
    private Integer eventStreamId;
    private Integer revertFromEventStreamId;
    private EventType eventType;
    private EventInitiatedBy initiatedBy;
    private long timestamp;
    private ResponseMessage responseMessage;
    private ResponseData responseData;

}
