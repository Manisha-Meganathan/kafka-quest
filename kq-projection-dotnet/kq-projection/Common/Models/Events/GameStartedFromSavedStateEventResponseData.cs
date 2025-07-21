using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class GameStartedFromSavedStateEventResponseData : ResponseData
    {
        [JsonPropertyName("startFromTime")]
        public long startFromTime { get; set; }

        [JsonPropertyName("puzzlePieces")]
        public List<JigsawPuzzlePieceResponse> puzzlePieces { get; set; }

        [JsonPropertyName("addedPieces")]
        public List<JigsawPieceCurrentPositionResponse> addedPieces { get; set; }
    }
}
