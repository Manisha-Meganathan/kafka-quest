package com.kafkaquest.kq.common.util.models.events.responsedata;

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
public class SaveStateResponseData extends ResponseData {
    private int toEventStreamId;
}
