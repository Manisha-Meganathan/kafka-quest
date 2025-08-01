package com.kafkaquest.kq.common.util.models.events.eventdata;

import com.kafkaquest.kq.common.util.models.events.other.JigsawPuzzlePiece;
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
public class GameStartedEventData extends EventData {

    private int puzzleImageId;

    private int horizontalSize;

    private int verticalSize;

    private List<JigsawPuzzlePiece> puzzlePieces;

}
