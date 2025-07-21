package com.kafkaquest.kq.aggregate.models;

import com.kafkaquest.kq.common.util.models.events.AggregateEvent;
import com.kafkaquest.kq.common.util.models.events.eventdata.GameStartedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceAddedEventData;
import com.kafkaquest.kq.common.util.models.events.eventdata.PieceRemovedEventData;
import com.kafkaquest.kq.common.util.models.events.other.ScoreCard;
import com.kafkaquest.kq.common.util.models.events.other.JigsawPuzzlePiece;
import com.kafkaquest.kq.common.util.models.events.enums.EventStatus;
import com.kafkaquest.kq.common.util.models.events.enums.Grade;
import com.kafkaquest.kq.common.util.models.events.other.TransportScoreCard;
import com.kafkaquest.kq.common.util.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kafkaquest.kq.aggregate.services.utils.CommonUtils.buildExceptionalAggregateEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JigsawPuzzleGame {

    private UUID gameId;

    private Long playerId;

    private Integer currentEventStreamId;

    // current status of the game
    private GameStatus gameStatus;

    // storing the timestamp of the first event
    private LocalDateTime firstEventTimestamp;

    // storing the timestamp of the last event
    private LocalDateTime lastEventTimestamp;

    private JigsawPuzzle jigsawPuzzle;

    private ScoreCard scoreCard;

    public AggregateEvent create(
        final ConsumeEvent gameStartedEvent,
        final List<JigsawPuzzlePieceWrapper> pieceWrappers
    ) {
        final var eventData = (GameStartedEventData) gameStartedEvent.getEventData();

        final var puzzleSize = eventData.getHorizontalSize() * eventData.getVerticalSize();

        if (puzzleSize != pieceWrappers.size()) {
            var exceptionMessage = Constants.INCORRECT_NUMBER_OF_PIECES;
            return buildExceptionalAggregateEvent(gameStartedEvent, exceptionMessage);
        }

        this.gameId = gameStartedEvent.getGameId();
        this.playerId = gameStartedEvent.getPlayerId();
        this.currentEventStreamId = 1;
        this.gameStatus = GameStatus.STARTED;
        this.firstEventTimestamp = DateTimeUtil.epochToDateTime(gameStartedEvent.getTimestamp());
        this.lastEventTimestamp = DateTimeUtil.epochToDateTime(gameStartedEvent.getTimestamp());
        final var puzzle = new JigsawPuzzle();
        puzzle.create(eventData, pieceWrappers);
        this.jigsawPuzzle = puzzle;
        this.scoreCard = new ScoreCard();

        eventData.setPuzzlePieces(getTransportJigsawPuzzlePieces(pieceWrappers));

        return AggregateEvent.builder()
                .gameId(gameStartedEvent.getGameId())
                .playerId(gameStartedEvent.getPlayerId())
                .eventStreamId(this.currentEventStreamId)
                .eventType(gameStartedEvent.getEventType())
                .initiatedBy(gameStartedEvent.getInitiatedBy())
                .timestamp(gameStartedEvent.getTimestamp())
                .eventStatus(EventStatus.OTHER)
                .eventData(eventData)
                .build();
    }

    private List<JigsawPuzzlePiece> getTransportJigsawPuzzlePieces(
        final List<JigsawPuzzlePieceWrapper> pieceWrappers
    ) {
        return pieceWrappers.stream()
                .map(JigsawPuzzlePieceWrapper::toJigsawPuzzlePiece)
                .collect(Collectors.toList());
    }

    public AggregateEvent addPuzzlePiece(final ConsumeEvent pieceAddedEvent) {

        final var eventData = (PieceAddedEventData) pieceAddedEvent.getEventData();

        if (this.jigsawPuzzle.isNotValidId(eventData.getPieceId())) {
            var exceptionMessage = Constants.getIncorrectPieceIdMessage(eventData.getPieceId());
            return buildExceptionalAggregateEvent(pieceAddedEvent, exceptionMessage);
        }

        if (this.jigsawPuzzle.isPositionOccupied(eventData.getMovedColumnPosition(), eventData.getMovedRowPosition())) {
            var exceptionMessage = Constants.POSITION_OCCUPIED;
            return buildExceptionalAggregateEvent(pieceAddedEvent, exceptionMessage);
        }

        this.lastEventTimestamp = DateTimeUtil.epochToDateTime(pieceAddedEvent.getTimestamp());
        this.currentEventStreamId = this.getNextEventStreamId();

        final boolean isPieceCorrectlyPositioned = this.jigsawPuzzle.addPiece(eventData);
        final var isGameEnded = this.jigsawPuzzle.hasAllCorrectlyPositioned();

        if (isPieceCorrectlyPositioned) {
            eventData.setHasMovedToCorrectPosition(Boolean.TRUE);
        } else {
            eventData.setHasMovedToCorrectPosition(Boolean.FALSE);
        }

        EventStatus aggregateEventStatus = EventStatus.OTHER;

        if (isGameEnded) {
            this.gameStatus = GameStatus.ENDED;
            calculateScore();

            aggregateEventStatus = EventStatus.GAME_ENDED;
            eventData.setScoreCard(
                new TransportScoreCard(
                    this.getScoreCard().getTimeSpent().toMillis(),
                    this.scoreCard.getGrade()
                )
            );
        }

        return AggregateEvent.builder()
                .gameId(pieceAddedEvent.getGameId())
                .playerId(pieceAddedEvent.getPlayerId())
                .eventStreamId(this.currentEventStreamId)
                .revertFromEventStreamId(pieceAddedEvent.getRevertFromEventStreamId())
                .eventType(pieceAddedEvent.getEventType())
                .initiatedBy(pieceAddedEvent.getInitiatedBy())
                .timestamp(pieceAddedEvent.getTimestamp())
                .eventStatus(aggregateEventStatus)
                .eventData(eventData)
                .build();
    }

    public AggregateEvent removePuzzlePiece(final ConsumeEvent pieceRemovedEvent) {
        final var eventData = (PieceRemovedEventData) pieceRemovedEvent.getEventData();

        if (this.jigsawPuzzle.isNotValidId(eventData.getPieceId())) {
            var exceptionMessage = Constants.getIncorrectPieceIdMessage(eventData.getPieceId());
            return buildExceptionalAggregateEvent(pieceRemovedEvent, exceptionMessage);
        }

        this.lastEventTimestamp = DateTimeUtil.epochToDateTime(pieceRemovedEvent.getTimestamp());
        this.currentEventStreamId = this.getNextEventStreamId();

        this.jigsawPuzzle.removePiece(eventData);

        return AggregateEvent.builder()
                .gameId(pieceRemovedEvent.getGameId())
                .playerId(pieceRemovedEvent.getPlayerId())
                .eventStreamId(this.currentEventStreamId)
                .revertFromEventStreamId(pieceRemovedEvent.getRevertFromEventStreamId())
                .eventType(pieceRemovedEvent.getEventType())
                .initiatedBy(pieceRemovedEvent.getInitiatedBy())
                .timestamp(pieceRemovedEvent.getTimestamp())
                .eventStatus(EventStatus.OTHER)
                .eventData(eventData)
                .build();
    }

    private void calculateScore() {
        final var timeSpent = Duration.between(firstEventTimestamp, lastEventTimestamp);
        this.scoreCard.setTimeSpent(timeSpent);
        this.scoreCard.setGrade(getGradeOnTimeSpent(timeSpent));
    }

    private Grade getGradeOnTimeSpent(final Duration duration) {
        final var durationInSeconds = duration.getSeconds();

        final var start = Duration.ZERO.getSeconds();
        final var firstCap = Duration.ofMinutes(2).getSeconds();
        final var secondCap = Duration.ofMinutes(5).getSeconds();

        if (durationInSeconds > start && durationInSeconds <= firstCap) {
            return Grade.EXCELLENT;
        } else if (durationInSeconds > firstCap && durationInSeconds <= secondCap) {
            return Grade.GOOD;
        } else {
            return Grade.BAD;
        }
    }

    private int getNextEventStreamId() {
        return this.currentEventStreamId + 1;
    }

}
