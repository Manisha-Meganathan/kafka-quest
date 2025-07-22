package com.kafkaquest.kq.common.util.models.kafkakeys;

import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateKey {

    private EventType eventType;
}
