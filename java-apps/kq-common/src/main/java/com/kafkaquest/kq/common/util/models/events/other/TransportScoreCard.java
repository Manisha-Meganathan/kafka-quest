package com.kafkaquest.kq.common.util.models.events.other;

import com.kafkaquest.kq.common.util.models.events.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportScoreCard {
    private Long timeSpent;
    private Grade grade;
}
