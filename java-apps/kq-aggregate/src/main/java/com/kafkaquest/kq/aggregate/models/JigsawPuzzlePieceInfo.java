package com.kafkaquest.kq.aggregate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JigsawPuzzlePieceInfo {

    private UUID pieceId;
    private int row;
    private int column;

}
