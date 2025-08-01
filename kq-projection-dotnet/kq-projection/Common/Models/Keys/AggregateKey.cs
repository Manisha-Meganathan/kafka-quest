namespace KQ_Projection.Common.Models.Keys;

using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

public class AggregateKey
{
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public EventType eventType { get; set; }
}
