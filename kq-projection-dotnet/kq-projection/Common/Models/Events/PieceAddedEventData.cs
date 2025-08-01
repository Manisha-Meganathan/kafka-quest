namespace KQ_Projection.Common.Models.Events;

using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

public class PieceAddedEventData : EventData
{
    [JsonPropertyName("pieceId")]
    public Guid pieceId { get; set; }


    [JsonPropertyName("movedRowPosition")]
    public int movedRowPosition { get; set; }


    [JsonPropertyName("movedColumnPosition")]
    public int movedColumnPosition { get; set; }

    [JsonPropertyName("hasMovedToCorrectPosition")]
    public bool hasMovedToCorrectPosition { get; set; }

    [JsonPropertyName("scoreCard")]
    public TransportScoreCard? scoreCard { get; set; }
}
