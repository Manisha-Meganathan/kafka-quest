using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;
using KQ_Projection.Common.Models.Jigsaw;

namespace KQ_Projection.Services.EventHandling;

public class PieceRemovedEventHandler : EventHandler
{
    public override ProjectionEventResponse Handle(byte[] eventBytes, ProjectionManager projectionManager)
    {
        AggregateEvent deserializedData = Deserialize(eventBytes, typeof(PieceRemovedEventData));
        JigsawPuzzleProjection? projection = projectionManager.GetProjection(deserializedData.gameId);

        if (projection == null)
        {
            Console.WriteLine("Projection doesn't exist!");
            return OnError(deserializedData);
        }

        return projection.RemovePuzzlePiece(deserializedData);
    }

    private ProjectionEventResponse OnError(AggregateEvent deserializedData)
    {
        PieceRemovedEventData pieceRemovedEventData = (PieceRemovedEventData)deserializedData.eventData;
        return new ProjectionEventResponse
        {
            gameId = deserializedData.gameId,
            playerId = deserializedData.playerId,
            eventStreamId = deserializedData?.eventStreamId,
            eventType = deserializedData.eventType,
            timestamp = deserializedData.timestamp,
            responseMessage = ResponseMessage.FAILURE,
            responseData = new PieceAddedEventResponseData()
            {
                pieceId = pieceRemovedEventData.pieceId

            }
        };
    }
}
