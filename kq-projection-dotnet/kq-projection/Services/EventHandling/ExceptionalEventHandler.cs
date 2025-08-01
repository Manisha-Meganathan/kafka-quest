using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;

namespace KQ_Projection.Services.EventHandling;

public class ExceptionalEventHandler : EventHandler
{
    public override ProjectionEventResponse Handle(byte[] eventBytes, ProjectionManager projectionManager)
    {
        var deserializedData = Deserialize(eventBytes, typeof(ExceptionalEventData));
        var eventData = (ExceptionalEventData)deserializedData.eventData;

        return new ProjectionEventResponse
        {
            gameId = deserializedData.gameId,
            playerId = deserializedData.playerId,
            eventType = deserializedData.eventType,
            timestamp = deserializedData.timestamp,
            responseMessage = ResponseMessage.FAILURE,
            responseData = new ExceptionalEventResponseData()
            {
                previousEventType = eventData.previousEventType,
                exceptionMessage = eventData.exceptionMessage
            }
        };
    }
}
