package com.kafkaquest.kq.common.util.models.events.eventdata;

import com.kafkaquest.kq.common.util.models.events.enums.DiscardReason;
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
public class GameDiscardedEventData extends EventData {
    private DiscardReason discardReason;
}
