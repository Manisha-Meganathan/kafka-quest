using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events;
public class ExceptionalEventData : EventData
{
    [JsonPropertyName("previousEventType")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventType previousEventType { get; set; }

    [JsonPropertyName("exceptionMessage")]
    public String exceptionMessage { get; set; }
}