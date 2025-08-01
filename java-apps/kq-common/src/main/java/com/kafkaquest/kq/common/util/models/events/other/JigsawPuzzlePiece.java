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
public class JigsawPuzzlePiece {

    private UUID pieceId;
    private int column;
    private int row;
    private byte[] pieceData;
}
