package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.eventdata.EventData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartFromSavedStateEventData extends EventData {
    private int fromEventStreamId;
}
