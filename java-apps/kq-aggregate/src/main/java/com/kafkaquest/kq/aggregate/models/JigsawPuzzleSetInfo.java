package com.kafkaquest.kq.aggregate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JigsawPuzzleSetInfo {

    private String path;

    private int imageId;

    private int verticalSize;

    private int horizontalSize;

    private List<String> piecesList;
}
