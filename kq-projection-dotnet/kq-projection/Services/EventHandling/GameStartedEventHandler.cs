using KQ_Projection.Common.Models.Events;
using KQ_Projection.Common.Models.Jigsaw;

namespace KQ_Projection.Services.EventHandling;

public class GameStartedEventHandler : EventHandler
{
    public override ProjectionEventResponse Handle(byte[] eventBytes, ProjectionManager projectionManager)
    {
        AggregateEvent deserializedData = Deserialize(eventBytes, typeof(GameStartedEventData));

        var projection = new JigsawPuzzleProjection();
        var response = projection.Create(deserializedData);
        projectionManager.AddProjection(projection);
        return response;
    }
}
