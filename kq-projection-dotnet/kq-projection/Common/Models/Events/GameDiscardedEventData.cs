using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events;

public class GameDiscardedEventData : EventData
{
    [JsonPropertyName("discardReason")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public DiscardReason discardReason {  get; set; }
}
