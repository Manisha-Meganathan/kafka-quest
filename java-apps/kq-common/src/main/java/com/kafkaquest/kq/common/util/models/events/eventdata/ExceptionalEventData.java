package com.kafkaquest.kq.common.util.models.events.eventdata;

import com.kafkaquest.kq.common.util.models.events.enums.EventType;
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
public class ExceptionalEventData extends EventData {

    private EventType previousEventType;

    private String exceptionMessage;

}
