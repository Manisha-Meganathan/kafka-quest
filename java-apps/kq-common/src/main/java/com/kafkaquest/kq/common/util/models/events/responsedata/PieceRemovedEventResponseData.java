package com.kafkaquest.kq.common.util.models.events.responsedata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PieceRemovedEventResponseData extends ResponseData {
    private UUID pieceId;
    private int removedRowPosition;
    private int removedColumnPosition;
}
