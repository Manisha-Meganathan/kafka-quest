namespace KQ_Projection.Common.Models.Events;

using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

public class GameStartedEventData : EventData
{
    [JsonPropertyName("puzzleImageId")]
    public int puzzleImageId { get; set; }

    [JsonPropertyName("horizontalSize")]
    public int horizontalSize { get; set; }

    [JsonPropertyName("verticalSize")]
    public int verticalSize { get; set; }

    [JsonPropertyName("puzzlePieces")]
    public List<JigsawPuzzlePiece> puzzlePieces { get; set; }
}
