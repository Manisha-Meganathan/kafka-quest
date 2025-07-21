package com.kafkaquest.kq.common.util.models.events.other;

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
public class JigsawPieceCurrentPositionResponse {
    private UUID pieceId;
    private int rowPosition;
    private int columnPosition;
}
