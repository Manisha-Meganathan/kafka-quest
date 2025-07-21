using Confluent.Kafka;
using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;
using KQ_Projection.Common.Models.Keys;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KQ_Projection.Services.EventHandling;

public class EventProcessor
{
    public static Message<ProjectionKey, ProjectionEventResponse> process(byte[] data, AggregateKey key) {
        
        ProjectionManager projectionManager = ProjectionManager.GetInstance();
        EventHandler eventHandler = EventHandlerFactory.GetHandler(key);
        ProjectionEventResponse response = eventHandler.Handle(data, projectionManager);
        ProjectionKey projectionKey = ProjectionKey.Create(response.playerId, response.gameId, response.eventType);

        return new Message<ProjectionKey, ProjectionEventResponse> { Key= projectionKey, Value= response};
    }
}
