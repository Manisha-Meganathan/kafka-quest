package com.kafkaquest.kq.api.KESAApiService.service;

import com.kafkaquest.kq.common.util.models.events.ProjectionEventResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UndeliveredResponsesManager {

    private final ConcurrentHashMap<UUID, List<ProjectionEventResponse>> undeliveredResponses = new ConcurrentHashMap<>();

    public void addOne(UUID gameId, ProjectionEventResponse response) {
        if (existsByGameId(gameId)) {
            undeliveredResponses.get(gameId).add(response);
        } else {
            var responses = new LinkedList<ProjectionEventResponse>();
            responses.add(response);
            undeliveredResponses.put(gameId, responses);
        }
    }

    public boolean existsByGameId(UUID gameId) {
        return undeliveredResponses.containsKey(gameId);
    }

    @Nullable
    public List<ProjectionEventResponse> getUndeliveredResponses(UUID gameId) {
        return undeliveredResponses.get(gameId);
    }

    public void removeUndeliveredResponses(UUID gameId) {
        undeliveredResponses.remove(gameId);
    }
}
