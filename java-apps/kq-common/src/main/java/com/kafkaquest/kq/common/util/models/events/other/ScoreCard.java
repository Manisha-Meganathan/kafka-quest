package com.kafkaquest.kq.common.util.models.events.other;

import com.kafkaquest.kq.common.util.models.events.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreCard {

    private Duration timeSpent;
    private Grade grade;

}
