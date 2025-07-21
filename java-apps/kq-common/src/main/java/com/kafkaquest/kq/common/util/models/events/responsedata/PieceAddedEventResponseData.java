package com.kafkaquest.kq.common.util.models.events.responsedata;

import com.kafkaquest.kq.common.util.models.events.other.TransportScoreCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PieceAddedEventResponseData extends ResponseData {
    private UUID pieceId;
    private int movedRowPosition;
    private int movedColumnPosition;
    private boolean hasGameEnded;
    private TransportScoreCard scoreCard;
}
