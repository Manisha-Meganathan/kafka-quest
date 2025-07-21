package com.kafkaquest.kq.common.util.models.events.responsedata;

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
public class GameDiscardEventResponseData extends ResponseData {
    private DiscardReason discardReason;
}
