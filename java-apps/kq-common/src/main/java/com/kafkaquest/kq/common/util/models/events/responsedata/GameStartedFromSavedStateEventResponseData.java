package com.kafkaquest.kq.common.util.models.events.responsedata;

import com.kafkaquest.kq.common.util.models.events.other.JigsawPieceCurrentPositionResponse;
import com.kafkaquest.kq.common.util.models.events.other.JigsawPuzzlePieceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStartedFromSavedStateEventResponseData extends ResponseData {
    private long startFromTime;
    private List<JigsawPuzzlePieceResponse> puzzlePieces;
    private List<JigsawPieceCurrentPositionResponse> addedPieces;
}
