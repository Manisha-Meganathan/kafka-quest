using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;

namespace KQ_Projection.Services.EventHandling;

public class DiscardGameEventHandler : EventHandler
{
    public override ProjectionEventResponse Handle(byte[] eventBytes, ProjectionManager projectionManager)
    {
        AggregateEvent deserializedData = Deserialize(eventBytes, typeof(GameDiscardedEventData));

        projectionManager.RemoveProjection(deserializedData.gameId);

        return new ProjectionEventResponse()
        {
            playerId = deserializedData.playerId,
            gameId = deserializedData.gameId,
            eventStreamId = null,
            revertFromEventStreamId = null,
            eventType = deserializedData.eventType,
            initiatedBy = deserializedData.initiatedBy,
            timestamp = deserializedData.timestamp,
            responseMessage = ResponseMessage.SUCCESS,
            responseData = new GameDiscardEventResponseData()     
            {
                discardReason = DiscardReason.CLIENT_DISCONTINUED
            }
        };
    }
}
