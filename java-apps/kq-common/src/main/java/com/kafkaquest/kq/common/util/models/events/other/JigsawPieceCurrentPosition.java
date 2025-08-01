package com.kafkaquest.kq.common.util.models.events.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JigsawPieceCurrentPosition {

    private UUID pieceId;
    private int rowPosition;
    private int columnPosition;
    private Boolean isInCorrectPosition;
}
