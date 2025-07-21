package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandlerResult {

    private AggregateKey aggregateKey;

    private AggregateEvent aggregateEvent;

}
