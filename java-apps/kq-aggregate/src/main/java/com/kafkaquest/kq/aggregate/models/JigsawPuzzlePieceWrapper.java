package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.other.JigsawPuzzlePiece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A Wrapper class to hold data and information of a jigsaw puzzle piece
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JigsawPuzzlePieceWrapper {

    private UUID pieceId;
    private int column;
    private int row;
    private byte[] pieceData;

    public JigsawPuzzlePieceInfo toJigsawPuzzlePieceInfo() {
        return new JigsawPuzzlePieceInfo(this.pieceId, this.row, this.column);
    }

    public JigsawPuzzlePiece toJigsawPuzzlePiece() {
        return new JigsawPuzzlePiece(this.pieceId, this.column, this.row, this.pieceData);
    }
}
