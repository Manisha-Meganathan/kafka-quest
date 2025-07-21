namespace KQ_Projection.Common.Models.Jigsaw;

using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;
using KQ_Projection.ExtensionMethods;

public class JigsawPuzzleProjection
{
    public Guid gameId { get; set; }

    public long playerId { get; set; }

    public TimeSpan elapsedTime { get; set; }

    public DateTime startedOn { get; set; }

    public int puzzleImageId { get; set; }

    public int verticalSize { get; set; }

    public int horizontalSize { get; set; }

    public List<JigsawPieceCurrentPosition> addedPieces { get; set; }

    public ProjectionEventResponse Create(AggregateEvent gameStartedEvent)
    {
        var eventdata = gameStartedEvent.eventData as GameStartedEventData;

        gameId = gameStartedEvent.gameId;
        playerId = gameStartedEvent.playerId;
        startedOn = startedOn.FromLong(gameStartedEvent.timestamp);
        elapsedTime = TimeSpan.Zero;
        puzzleImageId = eventdata.puzzleImageId;
        verticalSize = eventdata.verticalSize;
        horizontalSize = eventdata.horizontalSize;
        var puzzleSize = horizontalSize * verticalSize;
        addedPieces = new List<JigsawPieceCurrentPosition>(puzzleSize);

        Console.WriteLine($"Created new projection, gameId : {gameStartedEvent.gameId}");

        var puzzlePieceResponseList = MapToPuzzlePieceResponses(eventdata.puzzlePieces);

        return new ProjectionEventResponse()
        {
            playerId = gameStartedEvent.playerId,
            gameId = gameStartedEvent.gameId,
            eventStreamId = gameStartedEvent?.eventStreamId,
            revertFromEventStreamId = gameStartedEvent?.revertFromEventStreamId,
            eventType = gameStartedEvent.eventType,
            initiatedBy = gameStartedEvent.initiatedBy,
            timestamp = gameStartedEvent.timestamp,
            responseMessage = ResponseMessage.SUCCESS,
            responseData = new GameStartedEventResponseData()
            {
                puzzlePieces = puzzlePieceResponseList
            }
        };
    }

    private List<JigsawPuzzlePieceResponse> MapToPuzzlePieceResponses(List<JigsawPuzzlePiece> jigsawPuzzlePieces)
    {
        return jigsawPuzzlePieces
            .Select(piece =>
                new JigsawPuzzlePieceResponse()
                {
                    pieceId = piece.pieceId,
                    pieceData = Convert.ToBase64String(piece.pieceData)
                }
            )
            .ToList();
    }

    public ProjectionEventResponse AddPuzzlePiece(AggregateEvent pieceAddedEvent)
    {
        var currentEventTimestamp = DateTime.UtcNow.FromLong(pieceAddedEvent.timestamp);
        elapsedTime = elapsedTime.DurationBetweenTimes(startedOn, currentEventTimestamp);
        var eventData = pieceAddedEvent.eventData as PieceAddedEventData;

        var currentPiece = new JigsawPieceCurrentPosition()
        {
            pieceId = eventData.pieceId,
            columnPosition = eventData.movedColumnPosition,
            rowPosition = eventData.movedRowPosition,
            isInCorrectPosition = eventData.hasMovedToCorrectPosition
        };

        addedPieces.Add(currentPiece);
        Console.WriteLine($"Added new piece, gameId : {pieceAddedEvent.gameId}");

        var hasGameEnded = pieceAddedEvent.eventStatus == EventStatus.GAME_ENDED;

        return new ProjectionEventResponse()
        {
            playerId = pieceAddedEvent.playerId,
            gameId = pieceAddedEvent.gameId,
            eventStreamId = pieceAddedEvent.eventStreamId,
            revertFromEventStreamId = pieceAddedEvent?.revertFromEventStreamId,
            timestamp = pieceAddedEvent.timestamp,
            eventType = pieceAddedEvent.eventType,
            initiatedBy = pieceAddedEvent.initiatedBy,
            responseMessage = ResponseMessage.SUCCESS,
            responseData = new PieceAddedEventResponseData()
            {
                pieceId = eventData.pieceId,
                movedRowPosition = eventData.movedRowPosition,
                movedColumnPosition = eventData.movedColumnPosition,
                hasGameEnded = hasGameEnded,
                scoreCard = eventData.scoreCard,
            }
        };
    }

    public ProjectionEventResponse RemovePuzzlePiece(AggregateEvent pieceRemovedEvent)
    {
        var currentEventTimestamp = DateTime.UtcNow.FromLong(pieceRemovedEvent.timestamp);
        elapsedTime = elapsedTime.DurationBetweenTimes(startedOn, currentEventTimestamp);
        var eventData = pieceRemovedEvent.eventData as PieceRemovedEventData;

        ResponseMessage responseMessage = ResponseMessage.SUCCESS;
        addedPieces.RemoveAll(p => p.pieceId == eventData.pieceId);
        Console.WriteLine($"Removed a piece, gameId: {pieceRemovedEvent.gameId}");
        return new ProjectionEventResponse()
        {
            playerId = pieceRemovedEvent.playerId,
            gameId = pieceRemovedEvent.gameId,
            eventStreamId = pieceRemovedEvent.eventStreamId,
            revertFromEventStreamId = pieceRemovedEvent?.revertFromEventStreamId,
            eventType = pieceRemovedEvent.eventType,
            initiatedBy = pieceRemovedEvent.initiatedBy,
            timestamp = pieceRemovedEvent.timestamp,
            responseMessage = responseMessage,
            responseData = new PieceRemovedEventResponseData() { 
                pieceId = eventData.pieceId,
                removedRowPosition = eventData.removedRowPosition,
                removedColumnPosition = eventData.removedColumnPosition,
            }
        };
    }

}
