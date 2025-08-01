namespace KQ_Projection.Common.Models.Jigsaw;
using KQ_Projection.Common.Enums;
using System.Text.Json.Serialization;

public class TransportScoreCard
{
    [JsonPropertyName("timeSpent")]
    public long timeSpent { get; set; }

    [JsonPropertyName("grade")]
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public Grade grade {get; set;}
}
