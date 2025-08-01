package com.kafkaquest.kq.common.util.models.events.eventdata;

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
public class PieceRemovedEventData extends EventData {

    private UUID pieceId;
    private int removedRowPosition;
    private int removedColumnPosition;

}
