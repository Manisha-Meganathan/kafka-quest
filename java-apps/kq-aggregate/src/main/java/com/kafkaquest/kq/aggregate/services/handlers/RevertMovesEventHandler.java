package com.kafkaquest.kq.aggregate.services.handlers;

import com.kafkaquest.kq.aggregate.models.ConsumeEvent;
import com.kafkaquest.kq.aggregate.models.HandlerResult;
import com.kafkaquest.kq.aggregate.models.JigsawPuzzleGame;
import com.kafkaquest.kq.aggregate.services.JdbiConnection;
import com.kafkaquest.kq.aggregate.services.JigsawPuzzleGameManager;
import com.kafkaquest.kq.common.util.models.events.DomainEvent;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceAddedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceRemovedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.RevertMovesEventData;
import com.kafkaquest.kq.common.util.models.events.enums.EventInitiatedBy;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kafkaquest.kq.aggregate.models.Constants.FAILED_TO_FETCH_EVENTS_FROM_DB;
import static com.kafkaquest.kq.aggregate.models.Constants.FAILED_TO_MAP_DOMAIN_EVENTS;
import static com.kafkaquest.kq.aggregate.services.utils.CommonUtils.buildExceptionalAggregateEvent;
import static com.kafkaquest.kq.aggregate.services.utils.EventMapper.fromDboToConsumeEvent;

@Slf4j
public class RevertMovesEventHandler implements EventHandler {

    private static final String SELECT_EVENTS_TO_REVERT =
            "SELECT * FROM kq_events.domain_events WHERE game_id = :gameId AND event_stream_id > :eventStreamId ORDER BY event_stream_id DESC";

    @Override
    public List<HandlerResult> handle(byte[] event, JigsawPuzzleGameManager manager) {
        final var consumedEvent = deserialize(event, RevertMovesEventData.class);

        final var eventData = (RevertMovesEventData) consumedEvent.getEventData();

        final UUID gameId = consumedEvent.getGameId();
        final int fromEventStreamId = eventData.getEventStreamId();

        final Jdbi jdbi = JdbiConnection.getInstance();

        final var dataFetchingTry = Try.of(() -> fetchEventsToRevert(gameId, fromEventStreamId, jdbi));

        if (dataFetchingTry.isFailure()) {
            log.error(dataFetchingTry.getCause().getMessage());
            var exceptionalEvent = buildExceptionalAggregateEvent(consumedEvent, FAILED_TO_FETCH_EVENTS_FROM_DB);
            return List.of(new HandlerResult(getExceptionalEventKey(), exceptionalEvent));
        }

        final List<DomainEvent> domainEvents = dataFetchingTry.get();

        final var dataMappingTry = Try.of(() -> fromDboToConsumeEvent(domainEvents, EventInitiatedBy.AGGREGATE));

        if (dataMappingTry.isFailure()) {
            log.error(dataMappingTry.getCause().getMessage());
            var exceptionalEvent = buildExceptionalAggregateEvent(consumedEvent, FAILED_TO_MAP_DOMAIN_EVENTS);
            return List.of(new HandlerResult(getExceptionalEventKey(), exceptionalEvent));
        }

        final List<ConsumeEvent> eventsToNegate = dataMappingTry.get();
        final List<ConsumeEvent> negatedEvents = negateAll(eventsToNegate);

        final var aggregate = manager.getAggregate(consumedEvent.getGameId());

        return updateAggregateThenMap(negatedEvents, aggregate);
    }

    private List<DomainEvent> fetchEventsToRevert(UUID gameId, int fromEventStreamId, Jdbi jdbi) {
        return jdbi.withHandle(
                handle -> handle
                    .registerRowMapper(BeanMapper.factory(DomainEvent.class))
                    .createQuery(SELECT_EVENTS_TO_REVERT)
                    .bind("gameId", gameId)
                    .bind("eventStreamId", fromEventStreamId)
                    .mapTo(DomainEvent.class)
                    .list()
        );
    }

    private List<ConsumeEvent> negateAll(List<ConsumeEvent> eventsToNegate) {
        final EnumSet<EventType> consideringEventsFilter = EnumSet.of(EventType.PIECE_ADDED_EVENT, EventType.PIECE_REMOVED_EVENT);

        return eventsToNegate.stream()
                .filter(event -> consideringEventsFilter.contains(event.getEventType()))
                .map(this::negateOne)
                .collect(Collectors.toList());
    }

    private ConsumeEvent negateOne(ConsumeEvent eventToNegate) {
        if (eventToNegate.getEventType() == EventType.PIECE_ADDED_EVENT) {
            var eventData = (PieceAddedEventData) eventToNegate.getEventData();

            return ConsumeEvent.builder()
                .gameId(eventToNegate.getGameId())
                .playerId(eventToNegate.getPlayerId())
                .eventType(EventType.PIECE_REMOVED_EVENT)
                .revertFromEventStreamId(eventToNegate.getEventStreamId())
                .initiatedBy(eventToNegate.getInitiatedBy())
                .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                .eventData(
                    PieceRemovedEventData.builder()
                        .pieceId(eventData.getPieceId())
                        .removedRowPosition(eventData.getMovedRowPosition())
                        .removedColumnPosition(eventData.getMovedColumnPosition())
                        .build()
                )
                .build();
        } else {
            var eventData = (PieceRemovedEventData) eventToNegate.getEventData();
            return ConsumeEvent.builder()
                .gameId(eventToNegate.getGameId())
                .playerId(eventToNegate.getPlayerId())
                .eventType(EventType.PIECE_ADDED_EVENT)
                .revertFromEventStreamId(eventToNegate.getEventStreamId())
                .initiatedBy(eventToNegate.getInitiatedBy())
                .timestamp(DateTimeUtil.dateTimeToEpoch(LocalDateTime.now()))
                .eventData(
                    PieceAddedEventData.builder()
                        .pieceId(eventData.getPieceId())
                        .movedRowPosition(eventData.getRemovedRowPosition())
                        .movedColumnPosition(eventData.getRemovedColumnPosition())
                        .build()
                )
                .build();
        }
    }

    private List<HandlerResult> updateAggregateThenMap(List<ConsumeEvent> negatedEvents, JigsawPuzzleGame aggregate) {
        List<HandlerResult> aggregateEvents = new LinkedList<>();

        for (var event : negatedEvents) {
            switch (event.getEventType()) {
                case PIECE_ADDED_EVENT -> aggregateEvents.add(processPieceAddedEvent(event, aggregate));
                case PIECE_REMOVED_EVENT -> aggregateEvents.add(processPieceRemovedEvent(event, aggregate));
            }
        }
        return aggregateEvents;
    }

    private HandlerResult processPieceAddedEvent(ConsumeEvent event, JigsawPuzzleGame aggregate) {
        var aggregateEvent = aggregate.addPuzzlePiece(event);

        if (aggregateEvent.getEventType() == EventType.EXCEPTIONAL_EVENT){
            return new HandlerResult(getExceptionalEventKey(), aggregateEvent);
        }

        return new HandlerResult(PieceAddedEventHandler.getKey(), aggregateEvent);
    }

    private HandlerResult processPieceRemovedEvent(ConsumeEvent event, JigsawPuzzleGame aggregate) {
        var aggregateEvent = aggregate.removePuzzlePiece(event);

        if (aggregateEvent.getEventType() == EventType.EXCEPTIONAL_EVENT){
            return new HandlerResult(getExceptionalEventKey(), aggregateEvent);
        }

        return new HandlerResult(PieceRemovedEventHandler.getKey(), aggregateEvent);
    }

}
