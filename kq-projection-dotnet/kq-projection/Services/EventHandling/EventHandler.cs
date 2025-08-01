using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Events;
using KQ_Projection.Common.SerDes.JsonConverters;
using System.Text.Json;

namespace KQ_Projection.Services.EventHandling;

public abstract class EventHandler
{
    public abstract ProjectionEventResponse Handle(byte[] eventBytes, ProjectionManager projectionManager);
    protected AggregateEvent Deserialize(byte[] eventBytes, Type targetEventType)
    {
        try
        {
            JsonSerializerOptions options = new JsonSerializerOptions
            {
                Converters = { new CustomEventDataConverter(targetEventType) },
                PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
            };
#pragma warning disable CS8603
            return JsonSerializer.Deserialize<AggregateEvent>(eventBytes, options);
#pragma warning restore CS8603
        }
        catch (NotSupportedException ex)
        {
            throw new ApplicationException("Failed to deserialize value", ex);
        }
    }
}
