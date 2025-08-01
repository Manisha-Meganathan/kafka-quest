using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class GameStartedFromSavedStateEventData : EventData
    {
        [JsonPropertyName("puzzleImageId")]
        public int puzzleImageId {  get; set; }

        [JsonPropertyName("horizontalSize")]
        public int horizontalSize { get; set; }

        [JsonPropertyName("verticalSize")]
        public int verticalSize { get; set; }

        [JsonPropertyName("elapsedTime")]
        public long elapsedTime { get; set; }

        [JsonPropertyName("penaltyTime")]
        public long penaltyTime { get; set; }

        [JsonPropertyName("puzzlePieces")]
        public List<JigsawPuzzlePiece> puzzlePieces { get; set; }

        [JsonPropertyName("addedPieces")]
        public List<JigsawPieceCurrentPosition> addedPieces { get; set; }
    }
}
