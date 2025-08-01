package com.kafkaquest.kq.common.util.models.events.responsedata;

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
public class ExceptionalEventResponseData extends ResponseData {
    private EventType previousEventType;
    private String exceptionMessage;
}
