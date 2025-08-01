package com.kafkaquest.kq.aggregate.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaquest.kq.aggregate.exceptions.JigsawPiecesConfigException;
import com.kafkaquest.kq.aggregate.exceptions.JigsawPiecesNotFoundException;
import com.kafkaquest.kq.aggregate.models.JigsawPuzzlePieceWrapper;
import com.kafkaquest.kq.aggregate.models.JigsawPuzzleSetInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JigsawPuzzlePiecesManager {

    private static final String PUZZLE_PIECES_JSON_FILE = "/puzzle_pieces.json";

    private static JigsawPuzzlePiecesManager instance;

    private Map<Integer, List<JigsawPuzzlePieceWrapper>> cachedPiecesMap = new HashMap<>();

    private JigsawPuzzlePiecesManager() {
    }

    public static synchronized JigsawPuzzlePiecesManager getInstance() {
        if (instance == null) {
            instance = new JigsawPuzzlePiecesManager();
        }
        return instance;
    }

    public List<JigsawPuzzlePieceWrapper> mapToPuzzlePieces(int verticalSize, int horizontalSize) throws IOException {

        int key = verticalSize * horizontalSize;

        if (cachedPiecesMap.containsKey(key) && !cachedPiecesMap.get(key).isEmpty()) {
            return cachedPiecesMap.get(key);
        }

        List<JigsawPuzzlePieceWrapper> pieceWrappers = null;

        try (InputStream jsonStream = JigsawPuzzlePiecesManager.class.getResourceAsStream(PUZZLE_PIECES_JSON_FILE)) {
            if (jsonStream == null) {
                throw new JigsawPiecesConfigException("JSON file not found: " + PUZZLE_PIECES_JSON_FILE);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<JigsawPuzzleSetInfo> puzzleSetInfos = objectMapper.readValue(jsonStream, new TypeReference<>() {});

            for (JigsawPuzzleSetInfo puzzleSetInfo : puzzleSetInfos) {
                if (puzzleSetInfo.getVerticalSize() == verticalSize && puzzleSetInfo.getHorizontalSize() == horizontalSize) {
                    pieceWrappers = loadPieces(puzzleSetInfo);
                    cachedPiecesMap.put(key, pieceWrappers);
                }
            }
        }

        if (pieceWrappers == null) {
            throw new JigsawPiecesConfigException("Requested puzzle pieces config not found");
        }

        return pieceWrappers;
    }

    private List<JigsawPuzzlePieceWrapper> loadPieces(JigsawPuzzleSetInfo puzzleSetInfo) throws IOException {
        List<JigsawPuzzlePieceWrapper> puzzlePieces = new ArrayList<>();

        for (String imagePieceName : puzzleSetInfo.getPiecesList()) {
            String imagePath = puzzleSetInfo.getPath() + "/" + imagePieceName;

            try (InputStream is = JigsawPuzzlePiecesManager.class.getResourceAsStream(imagePath)) {
                if (is == null) {
                    throw new JigsawPiecesNotFoundException("Jigsaw piece not found: " + imagePath);
                }

                byte[] pieceDataBytes = is.readAllBytes();

                int column = Integer.parseInt(imagePieceName.split("_")[0]);
                int row = Integer.parseInt(imagePieceName.split("_")[1].split("\\.")[0]);
                UUID id = UUID.randomUUID();

                JigsawPuzzlePieceWrapper piece = new JigsawPuzzlePieceWrapper(id, column, row, pieceDataBytes);
                puzzlePieces.add(piece);
            }
        }

        if (puzzlePieces.isEmpty() || puzzlePieces.size() != puzzleSetInfo.getPiecesList().size()) {
            throw new JigsawPiecesNotFoundException("Incorrect number of jigsaw pieces: " + puzzlePieces.size());
        }

        return puzzlePieces;
    }

}
