package com.kafkaquest.kq.aggregate.services;

import com.kafkaquest.kq.aggregate.models.JigsawPuzzleGame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JigsawPuzzleGameManager {

    private static JigsawPuzzleGameManager instance;

    private final Map<UUID, JigsawPuzzleGame> gameSessionAggregates;

    private JigsawPuzzleGameManager() {
        this.gameSessionAggregates = new HashMap<>();
    }

    public static synchronized JigsawPuzzleGameManager getInstance() {
        if (instance == null) {
            instance = new JigsawPuzzleGameManager();
        }
        return instance;
    }

    public synchronized JigsawPuzzleGame getAggregate(UUID gameId) {
        return this.gameSessionAggregates.get(gameId);
    }

    public synchronized void addAggregate(JigsawPuzzleGame jigsawPuzzleGame) {
        this.gameSessionAggregates.put(jigsawPuzzleGame.getGameId(), jigsawPuzzleGame);
    }

    public synchronized void removeAggregate(UUID gameId) {
        this.gameSessionAggregates.remove(gameId);
    }

}
