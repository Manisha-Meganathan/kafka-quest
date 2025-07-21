namespace KQ_Projection.Common.Models.Events;

using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

public class AggregateEvent
{
    [JsonPropertyName("gameId")]
    public Guid gameId { get; set; }

    [JsonPropertyName("playerId")]
    public long playerId { get; set; }

    [JsonPropertyName("eventStreamId")]
    public int? eventStreamId { get; set; }

    [JsonPropertyName("revertFromEventStreamId")]
    public int? revertFromEventStreamId { get; set; }

    [JsonPropertyName("eventType")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventType eventType { get; set; }

    [JsonPropertyName("initiatedBy")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventInitiatedBy initiatedBy { get; set; }

    [JsonPropertyName("timestamp")]
    public long timestamp { get; set; }


    [JsonPropertyName("eventStatus")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventStatus eventStatus { get; set; }


    [JsonPropertyName("eventData")]
    public EventData eventData { get; set; }
}
