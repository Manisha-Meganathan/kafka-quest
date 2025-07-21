package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.eventdata.GameStartedEventData;
import com.kafkaquest.kq.common.util.models.events.other.JigsawPieceCurrentPosition;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceAddedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceRemovedEventData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JigsawPuzzle {

    private int puzzleImageId;

    private int horizontalSize;

    private int verticalSize;

    private Map<UUID, JigsawPuzzlePieceInfo> pieces;

    private Map<UUID, JigsawPieceCurrentPosition> addedPieces;

    public void create(
        final GameStartedEventData eventData,
        final List<JigsawPuzzlePieceWrapper> pieceWrappers
    ) {
        this.puzzleImageId = eventData.getPuzzleImageId();
        this.horizontalSize = eventData.getHorizontalSize();
        this.verticalSize = eventData.getVerticalSize();
        this.pieces = this.getPuzzlePieces(pieceWrappers);
        this.addedPieces = new HashMap<>(this.getPuzzleSize());
    }

    private Map<UUID, JigsawPuzzlePieceInfo> getPuzzlePieces(
        final List<JigsawPuzzlePieceWrapper> pieceWrappers
    ) {
        return pieceWrappers.stream()
                .map(JigsawPuzzlePieceWrapper::toJigsawPuzzlePieceInfo)
                .collect(Collectors.toMap(JigsawPuzzlePieceInfo::getPieceId, p -> p));
    }

    // returns a boolean to indicate whether added piece is correctly positioned or not
    public boolean addPiece(PieceAddedEventData eventData) {
        final var pieceId = eventData.getPieceId();

        final var piece = this.pieces.get(pieceId);

        final boolean isCorrectRow = piece.getRow() == eventData.getMovedRowPosition();
        final boolean isCorrectColumn = piece.getColumn() == eventData.getMovedColumnPosition();

        final boolean isInCorrectPosition = isCorrectColumn && isCorrectRow;

        if (this.addedPieces.containsKey(pieceId)) {

            var pieceCurrentPosition = this.addedPieces.get(pieceId);
            pieceCurrentPosition.setColumnPosition(eventData.getMovedColumnPosition());
            pieceCurrentPosition.setRowPosition(eventData.getMovedRowPosition());
            pieceCurrentPosition.setIsInCorrectPosition(isInCorrectPosition);
        } else {

            var pieceCurrentPosition = JigsawPieceCurrentPosition.builder()
                    .pieceId(pieceId)
                    .columnPosition(eventData.getMovedColumnPosition())
                    .rowPosition(eventData.getMovedRowPosition())
                    .isInCorrectPosition(isInCorrectPosition)
                    .build();

            this.addedPieces.put(piece.getPieceId(), pieceCurrentPosition);
        }

        return isInCorrectPosition;
    }

    public void removePiece(PieceRemovedEventData eventData) {
        this.addedPieces.remove(eventData.getPieceId());
    }

    public boolean hasAllCorrectlyPositioned() {
        final var puzzleSize = this.getPuzzleSize();
        final var positionedPieces = this.addedPieces.values();

        if (puzzleSize != positionedPieces.size()) {
            return false;
        }

        return positionedPieces
                .stream()
                .allMatch(p -> p.getIsInCorrectPosition().equals(Boolean.TRUE));
    }

    public boolean isNotValidId(UUID pieceId) {
        return !this.pieces.containsKey(pieceId);
    }

    public int getPuzzleSize() {
        return this.horizontalSize * this.verticalSize;
    }

    public boolean isPositionOccupied(int column, int row) {
        return this.addedPieces.values()
                .stream()
                .anyMatch(p -> p.getColumnPosition() == column && p.getRowPosition() == row);
    }

}
