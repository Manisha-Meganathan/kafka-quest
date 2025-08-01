using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Keys;

public class ProjectionKey
{
    [JsonPropertyName("playerId")]
    public long playerId { get; set; }

    [JsonPropertyName("gameId")]
    public Guid gameId { get; set; }

    [JsonPropertyName("eventType")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventType eventType { get; set; }

    public static ProjectionKey Create(long playerId, Guid gameId, EventType eventType) { 
        return new ProjectionKey {  playerId = playerId, gameId = gameId, eventType = eventType };
    }
}
